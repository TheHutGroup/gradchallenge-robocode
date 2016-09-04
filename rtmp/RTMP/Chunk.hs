module Chunk where

import Data.Foldable
import Data.Bits
import Data.Binary
import Data.Word
import ByteStream
import Control.Applicative

{-
 Larger chunk sizes are good for CPU usage but commit to larger writes.
 On lower bandwidth systems, large low priority video chunks can block essential chunks from high priority streams like audio and control

 Smaller chunk sizes are not good when a high bit rate is required

 The chunk size is maintained independently on both the client and the server

-}

data BasicHeader = OneByte Word8 | TwoByte Word8 Word8 | ThreeByte Word8 Word16  deriving (Eq, Ord, Show, Read)
-- Header Field - 1, 2, 3 bytes long
-- fmt -> Message Header

instance Serialize BasicHeader where
    put (OneByte header) = putWord8 header
    put (TwoByte header cid) = putWord8 header >> putWord8 cid
    put (ThreeByte header cid) = putWord8 header >> putWord16le cid

    get = do
      fmtCID <- getWord8
      let csID = fmtCID .&. (shift 1 6 - 1)
          fmt = fmtCID .&. (shift 3 6)
      case csID of
        0 -> do
             chunkId <- getWord8
             return (TwoByte fmt chunkId)
        1 -> do
             chunkId <- getWord16le
             return (ThreeByte fmt chunkId)
        x -> return (OneByte x)

type MsgHdrTimeStamp = (Word8, Word8, Word8)
type MsgHdrTimeStampDelta = (Word8, Word8, Word8)    
type MsgHdrMsgLength = (Word8, Word8, Word8)
-- SetChunkSize: First bit of payload is 0, rest 31 bits are new chunk size
-- AbortMessage: Notifies that you can discard the partially recieved messages for a particular chunk stream id
-- Send an acknowledgement when you have received bytes equal to the window size, the payload is the number of bytes recieved so far
-- WindowAck Used to establish the window acknowledgement size
-- Set Peer bandwidth is used to set the window size of the peer
-- This should elicit a WindowAck message, with the new window size
-- Video - 9, Audio - 8
data MsgHdrTypeId = SetChunkSize1 Word32 | AbortMessage2 Word32 | Ack3 Word32 | WindowAck5 Word32 | SetPeerBandwidth6 Word32 Limit
data Limit = Hard0 | Soft1 | Dynamic2
type MsgStreamId = (Word8, Word8, Word8, Word8)    
data MessageHeader = MsgHdr0 MsgHdrTimeStamp MsgHdrMsgLength MsgHdrTypeId MsgStreamId
                   | MsgHdr1 MsgHdrTimeStampDelta MsgHdrMsgLength MsgHdrTypeId
                   | MsgHdr2 MsgHdrTimeStampDelta 
                   | MsgHdr3 (Maybe MsgTimeStampDelta)

{-
Default maximum chunk size is 128
Type 0 chunks must be used at the start of a stream
If the absolute timestamp if greater than 0xFFFFFF (3 bytes), then it must 
be that largest value indicating the presence of Extended Timestamp field.

The MsgHdrTypeId is distinguishes the protocol control message
-}           

data ChunkHeader = ChunkHeader {
          basicHeader :: BasicHeader,
          messageHeader :: ByteStream,
          extendedTimeStamp :: ByteStream
      } deriving (Eq, Ord, Show, Read)

data Chunk = Chunk {
        header :: ChunkHeader,
        chunkData :: ByteStream
   }



