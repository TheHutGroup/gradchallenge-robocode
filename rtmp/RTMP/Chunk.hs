module Chunk where


{-
 Larger chunk sizes are good for CPU usage but commit to larger writes.
 On lower bandwidth systems, large low priority video chunks can block essential chunks from high priority streams like audio and control

 Smaller chunk sizes are not good when a high bit rate is required

 The chunk size is maintained independently on both the client and the server

-}

data ChunkHeader = ChunkHeader {
          basicHeader :: ByteStream,
          messageHeader :: ByteStream,
          extendedTimeStamp :: ByteStream
      }

                 
