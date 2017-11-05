#!/usr/bin/env python

import configReader
import display
import rfidWrapper

display = display.Display()
RFIDReader = rfidWrapper.RFID_Wrapper()
    
def initialize():
  configReader.initialize()
  RFIDReader.Start()
  
def cleanup():
  display.setDisplay('Bye bye', ':(')
  display.cleanup()
  RFIDReader.stop()
  
#Display
def setDisplay(aFirstRow, aSecondRow):
  display.setDisplay(aFirstRow, aSecondRow)
  
def setDefaultDisplayValues(aFirstRow, aSecondRow):
  display.set_default_values(aFirstRow, aSecondRow)

#ConfigReader  
def getIniValue(aSection, aKey):
  return configReader.getIniValue(aSection, aKey)

#RFIDReader
def setRFIDCallback(aCallback):
  RFIDReader.setCallback(aCallback)
  
