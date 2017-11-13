#!/usr/bin/env python
# -*- coding: utf8 -*-

import FB_Status
import feeling_rest
import feeling_IO
from datetime import datetime, timedelta

class User_Mode:
  BUTTON_REPEAT_DELAY = timedelta(seconds=5)
  
  def __init__(self, aStatus):
    self.status = aStatus
    self.Button_Activate_Time = datetime.min
  
  def ButtonPressed(self, button):
    if self.Button_Activate_Time < datetime.now():
      error = feeling_rest.addFeelingBoardEntry(self.status.RFID_uid, button)
      self.Button_Activate_Time = datetime.now() + User_Mode.BUTTON_REPEAT_DELAY
      if not error:
        if self.status.Code:
          feeling_IO.setDisplay('Danke ' + self.status.RFID_name, 'Dein Code: ' + self.status.Code, 5)
        else:
          feeling_IO.setDisplay('Danke ' + self.status.RFID_name, 'fuers Mitmachen!', 5)
      else:
        feeling_IO.setDisplay('Error while', 'adding Entry')
    else:
      feeling_IO.setDisplay('nicht so schnell', 'Cowboy') 
  
  def HotButtonPressed(self):
    feeling_rest.callHipchatApi()
    
  def userChanged(self, aUid, aUserName):
    if (aUid == '0'):
      self.modeChanged()
    else:
      feeling_IO.setDisplay('Hallo ' + aUserName, 'Wie geht\'s?')
    
  def modeChanged(self):
    feeling_IO.setDefaultDisplayValues('Hallo', 'Wie geht\'s dir?')
    feeling_IO.setDisplay('Hallo', 'Wie geht\'s dir?')