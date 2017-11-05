#!/usr/bin/env python

import configReader
import display
import rfidWrapper
from pygame import mixer
import buttons

display = display.Display()
RFIDReader = rfidWrapper.RFID_Wrapper()
    
def initialize():
  configReader.initialize()
  RFIDReader.Start()
  mixer.init()
  buttons.initialize()
  
def cleanup():
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
  
#Sound
def playSound(aSoundFile):
  mixer.music.load(aSoundFile)
  mixer.music.play()
  
#Buttons
def setButtonCallbacks(aButtonCallback, aHotButtonCallback):
  buttons.setCallbacks(aButtonCallback, aHotButtonCallback)
