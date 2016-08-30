module Client where

import Utils

{-

   Need a way of checking whether the connection is terminated.
   

-}

handshake :: Comm Bool
handshake = do
  sendChunk c0
  sendChunk c1
  chunkS1 <- await lenS1
  sendChunk c2
  chunkS2 <- await lenS2


       
c0 :: Stream Byte
c0 = foldM sendByte clientVersion

-- rtmp version requested by the client
clientVersion = bytes [3]

-- Consider packing the C0, C1.. packets into a data Declaration tying together the length and the content                
                
lenC0 = 1
lenC1 = 1536
lenC2 = 1536

c1 :: Stream Byte
c1 = do
   -- this timestamp is the epoch used as a reference for all future time considerations
  -- in order to synchronize multiple streams, the client could send the epoch as the current time relative to the universal epoch
  foldM sendByte clientEpoch
  foldM sendByte (bytes (replicate 4 0))
  foldM sendByte (genRandom 1528)

clientEpoch :: ByteStream
clientEpoch = bytes [0, 0, 0, 0]

-- Whats the need over here for sending these 1528 random bytes
-- its apprently to distinguish between its handshake and the handshake initiated by the peer

c2 :: ByteStream -> ByteStream -> ByteStream -> Stream Byte
c2 serverEpoch timeAtWhichEpochRead s2RandomData = do
  foldM sendByte serverTimeStamp
  foldM sendByte timeAtWhichEpochRead
  foldM sendByte s1RandomData


{-
   can use the difference between the timestamp at which the server read the 
   timestamp and the timestamp at which the c1 was generated and sent as a measure
   of the bandwidth consumption
-}


-- the chunk size is configurable and can be set using a set chunk size control message
-- the chunk size is maintained independently on both systems

