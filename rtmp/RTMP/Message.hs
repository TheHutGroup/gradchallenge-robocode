module Message where

import Data.Binary    
import Data.Bits
import Data.ByteString

    
data Message = Message { header :: Header, payload :: ByteString }

data Header = Header {
        messageType :: Word8,
        length :: (Word8, Word8, Word8),
        timestamp :: (Word8, Word8, Word8, Word8),
        messageStream :: (Word8, Word8, Word8)
      }
