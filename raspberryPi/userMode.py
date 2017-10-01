#!/usr/bin/env python
# -*- coding: utf8 -*-

import FB_Status
import feeling_rest
from datetime import datetime, timedelta
import RPi.GPIO as GPIO

class User_Mode:
  GREAT_JOB_SOUND = 24
  BUTTON_REPEAT_DELAY = timedelta(seconds=5)
  
  def __init__(self, aStatus, aDisplay):
    self.status = aStatus
    self.display = aDisplay
    self.Button_Activate_Time = datetime.min
  
  def ButtonPressed(self, button):
    if self.Button_Activate_Time < datetime.now():
      error = feeling_rest.addFeelingBoardEntry(self.status.RFID_uid, button)
      self.Button_Activate_Time = datetime.now() + User_Mode.BUTTON_REPEAT_DELAY
      if not error:
        self.display.setDisplay('Danke ' + self.status.RFID_name + ' ' + str(button), 'fuers Mitmachen!')
      else:
        self.display.setDisplay('Error while', 'adding Entry')
    else:
      self.display.setDisplay('nicht so schnell', 'Cowboy') 
  
  def HotButtonPresses(self):
    self.makeGreatJobSound()
    
  def makeGreatJobSound (self):
    GPIO.output(User_Mode.GREAT_JOB_SOUND, GPIO.HIGH)
    time.sleep(0.1)
    GPIO.output(User_Mode.GREAT_JOB_SOUND, GPIO.LOW)
    self.display.setDisplay('Test1', 'Test2', 4)