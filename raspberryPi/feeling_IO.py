#!/usr/bin/env python

import configReader
import display
import rfidWrapper
from pygame import mixer
import os
import buttons
import time
import RPi.GPIO as GPIO

display = display.Display()
RFIDReader = rfidWrapper.RFID_Wrapper()

GREAT_JOB_SOUND = 24
    
def getFilePath(aFilePath):
    return os.path.join(os.path.dirname(__file__), aFilePath)
    
def initialize():
  configReader.initialize()
  RFIDReader.start()
  mixer.init()
  buttons.initialize()
  GPIO.setup(GREAT_JOB_SOUND, GPIO.OUT)
  
def cleanup():
  display.cleanup()
  RFIDReader.stop()
  
#Display
def setDisplay(aFirstRow, aSecondRow, seconds=0, style=2):
  display.setDisplay(aFirstRow, aSecondRow, seconds, style)
  
def setDefaultDisplayValues(aFirstRow, aSecondRow):
  display.set_default_values(aFirstRow, aSecondRow)

def resetDisplay():
  display.resetDisplay()

#ConfigReader  
def getIniValue(aSection, aKey):
  return configReader.getIniValue(aSection, aKey)

#RFIDReader
def setRFIDCallback(aCallback):
  RFIDReader.setCallback(aCallback)
  
#Sound
def playSound(aSoundFile):
  mixer.music.load(getFilePath(aSoundFile))
  mixer.music.play()
  
def makeGreatJobSound ():
  GPIO.output(GREAT_JOB_SOUND, GPIO.HIGH)
  time.sleep(0.1)
  GPIO.output(GREAT_JOB_SOUND, GPIO.LOW)
    
#Buttons
def setButtonCallbacks(aButtonCallback, aHotButtonCallback):
  buttons.setCallbacks(aButtonCallback, aHotButtonCallback)
