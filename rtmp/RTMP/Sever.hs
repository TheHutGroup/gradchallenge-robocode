module Server where

import Utils

-- Consider putting these configs into a reader
handshake :: Comm Bool
handshake = do
  clientVersion <- await lenC0
  if supports clientVersion
  then do
    s0 clientVersion
    s1 clientVersion
    chunkC1 <- await lenC1
    s2 clientVersion chunkC1
    chunkC2 <- await lenC2
  else terminateConnection


lenS0 = 1
lenS1 = 1536
lenS2 = 1536

s0 :: Byte -> Stream Byte
s0 = do
  foldM sendByte serverVersion
serverVersion = bytes [3]

-- rtmp version selected by the server

s1 :: Stream Byte
s1 = do
  foldM sendByte serverEpoch
  foldM sendByte (bytes (replicate 4 0))
  mapM_ sendByte (genRandom 1528)

serverEpoch :: [Byte]
serverEpoch = bytes [0, 0, 0, 0]

s2 :: ByteStream -> ByteStream -> Stream Byte
s2 clientEpoch timeAtWhichEpochRead c1RandomData = do
  foldM sendByte clientTimeStamp
  foldM sendByte timeAtWhichEpochRead
  foldM sendByte c1RandomData
