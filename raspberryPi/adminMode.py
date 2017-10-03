#!/usr/bin/env python
# -*- coding: utf8 -*-

import FB_Status
import feeling_rest
import netifaces as ni
import os


class Admin_Mode:
  
  configJobs = ('NetConfig', 'Restart', 'Shutdown')
  
  def __init__(self, aStatus, aDisplay):
    self.status = aStatus
    self.display = aDisplay
    self.jobToApply = ''
  
  def ButtonPressed(self, button):
    if (button < len(Admin_Mode.configJobs)):
      self.jobToApply = Admin_Mode.configJobs[button];
      self.display.setDisplay(Admin_Mode.configJobs[button], 'Mit HotButton bestaetigen')
    else:
      self.jobToApply = '';
      self.display.setDisplay('noch nicht implementiert', 'Sorry :(')
  
  def HotButtonPressed(self):
    self.applyConfig()
    
  def applyConfig (self):
    if (self.jobToApply == 'NetConfig'):
      interfaces = ''
      for iface in ni.interfaces():   
        ipv4s = ni.ifaddresses(iface).get(ni.AF_INET, [])
        for entry in ipv4s:
          addr = entry.get('addr')
          if not addr:
            continue
          interfaces = interfaces + ' ' + addr
      self.display.setDisplay('Interfaces:', interfaces)
    elif (self.jobToApply == 'Restart'):
      self.display.setDisplay('Restarting now', 'please wait')
      os.system('sudo shutdown -r now')
    elif (self.jobToApply == 'Shutdown'):
      self.display.setDisplay('Shutting down', 'Bye cruel world!')
      os.system('sudo shutdown -h now')
    self.status.resetMode()
    
    
    