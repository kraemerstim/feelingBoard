#!/usr/bin/env python
# -*- coding: utf8 -*-

import MFRC522
import time
from threading import Thread
  
class RFID_Wrapper:
  
  RFIDReader = MFRC522.MFRC522()

  def __init__(self):
    self.__currentChipUID = "0" 
    
  def getRFIDUID(self):
    (status, TagType) = self.RFIDReader.MFRC522_Request(self.RFIDReader.PICC_REQIDL)
    if status == self.RFIDReader.MI_ERR:
      (status, TagType) = self.RFIDReader.MFRC522_Request(self.RFIDReader.PICC_REQIDL)
    if status == self.RFIDReader.MI_OK:
      return self.RFIDReader.MFRC522_Anticoll()
    else:
      return (status, TagType)

  def getChipUID(self):
    return self.__currentChipUID
    
  def threaded_RFID_READER(self):
    while self.__continue_reading:
      (status,uid) = self.getRFIDUID()
      if status == self.RFIDReader.MI_OK:
        # Get the UID of the card
        (status,uid) = self.RFIDReader.MFRC522_Anticoll()
        if status == self.RFIDReader.MI_OK:
          # Print UID
          uid_string = str(uid[0])+str(uid[1])+str(uid[2])+str(uid[3])+str(uid[4])
          self.__currentChipUID = uid_string
      else:
        self.__currentChipUID = "0"
      self.__callback(self.__currentChipUID)
      time.sleep(0.1)

  def start(self, callback):
    self.__continue_reading = True
    self.__callback = callback
    Thread(target=self.threaded_RFID_READER).start()

  def stop(self):
    self.__continue_reading = False
