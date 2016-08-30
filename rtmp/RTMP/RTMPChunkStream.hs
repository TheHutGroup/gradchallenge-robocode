
{-


Integer fields are transferred in big endian - byte 0 is the byte shown

bit 0 is the most significant bit


Data is byte aligned

All padding fields should have the value 0

Timestamps are expressed in milliseconds relative to an epoch
32 bits long
Synchronization must be taken care of - 
Should use serial number arithmetic

Timestamps are relative to each other - consecutive ones are within, 2^31 - 1 of each other




-}

-- Put this into its own types module


data Message = Msg TimestampType PayloadType MessageStreamID

data Chunk = Chunk Timestamp Length TypeId

type Timestamp = (Byte, Byte, Byte, Byte)
type Length = (Byte, Byte, Byte)
type TypeId = Byte
type MessageStreamID = (Byte, Byte, Byte, Byte) -- Stored in little endian format
    


    
