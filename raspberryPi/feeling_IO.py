#!/usr/bin/env python

import configReader
import display

display = display.Display()

def initialize():
  configReader.initialize()
  
def setDisplay(aFirstRow, aSecondRow):
  display.setDisplay(aFirstRow, aSecondRow)
  
def setDefaultDisplayValues(aFirstRow, aSecondRow):
  display.set_default_values(aFirstRow, aSecondRow)
  
def getIniValue(aSection, aKey)
  return configReader.getIniValue(aSection, aKey)
  
def cleanup():
  display.setDisplay('Bye bye', ':(')
  display.cleanup()