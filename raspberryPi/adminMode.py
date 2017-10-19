#!/usr/bin/env python
# -*- coding: utf8 -*-

import FB_Status
import feeling_rest
import netifaces as ni
import os
import time

class Admin_Mode:
  
  configJobs = ('Sound', 'NetConfig', 'Restart', 'Shutdown', 'Update')
  
  def __init__(self, aStatus, aDisplay):
    self.status = aStatus
    self.display = aDisplay
    self.jobToApply = 0
    self.display.setDisplay(Admin_Mode.configJobs[self.jobToApply], 'Mit Rot bestaetigen')
  
  def ButtonPressed(self, button):
    if (button == 0):
      self.jobToApply = (self.jobToApply -1) % len(Admin_Mode.configJobs)
    if (button == 1):
      self.jobToApply = (self.jobToApply +1) % len(Admin_Mode.configJobs)
    if (button == 4):
      self.applyConfig()
    if (button == 5):
      self.status.resetMode()
      return

    self.display.setDisplay(Admin_Mode.configJobs[self.jobToApply], 'Mit Rot bestaetigen')
  
  def HotButtonPressed(self):
    pass
    
  def applyConfig (self):
    chosenJob = Admin_Mode.configJobs[self.jobToApply]
    if (chosenJob == 'NetConfig'):
      interfaces = ''
      for iface in ni.interfaces():   
        ipv4s = ni.ifaddresses(iface).get(ni.AF_INET, [])
        for entry in ipv4s:
          addr = entry.get('addr')
          if not addr:
            continue
          interfaces = interfaces + ' ' + addr
      self.display.setDisplay('Interfaces:', interfaces)
    elif (chosenJob == 'Restart'):
      self.display.setDisplay('Restarting now', 'please wait')
      time.sleep(1)
      os.system('sudo shutdown -r now')
    elif (chosenJob == 'Update'):
      self.display.setDisplay('Updating services', 'please wait')
      time.sleep(1)
      scriptPath = os.path.join(os.path.dirname(__file__), '../updateFB.sh')
      os.system(scriptPath)
    elif (chosenJob == 'Shutdown'):
      self.display.setDisplay('Shutting down', 'Bye cruel world!')
      time.sleep(1)
      os.system('sudo shutdown -h now')
    elif (chosenJob == 'Sound'):
      self.status.setMode('Sound')