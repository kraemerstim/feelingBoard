#!/usr/bin/env python

import feeling_rest
import feeling_IO
import RPi.GPIO as GPIO

class FB_Status:
  LED = 20
    
  def __init__(self):
    GPIO.setup(FB_Status.LED, GPIO.OUT) 
    
  def initialize(self):
    self.RFID_uid = '0'
    self.RFID_name = 'Anton'
    self.RFID_role = 'User'
    self.Mode = 'User'
    self.Code = None

  def rfid_id_callback(self, uidString):
    if (self.RFID_uid != uidString):
      self.RFID_uid = uidString;
      try:
        account = feeling_rest.getAccountByUid(self.RFID_uid)
        if account:
          name = account.json()['name']
          role = account.json()['role']
          self.Code = account.json()['code']
        else:
          name = 'Error'
          role = 'Error'
      except:
        name = 'Error'
        role = 'Error'
      
      if not role:
        role = 'User'
       
      if not name:
        name = 'Anton'
      
      self.RFID_name = name
      self.RFID_role = role
            
      self.rfid_changed()
      
  def rfid_changed(self):
    if (self.RFID_uid != '0'):
      GPIO.output(FB_Status.LED, GPIO.HIGH)
      self.setMode(self.RFID_role)
    else:
      GPIO.output(FB_Status.LED, GPIO.LOW)
      
    self.__userCallback(self.RFID_uid, self.RFID_name)
  
  def resetMode(self):
    self.setMode('User')
   
  def setMode(self, aMode):
    if (aMode != self.Mode):
      self.Mode = aMode
      print('Mode = ' + self.Mode)
      self.__modeCallback(self.Mode)
  
  def setCallbacks(self, aUserCallback, aModeCallback):
    self.__userCallback = aUserCallback
    self.__modeCallback = aModeCallback
    
  def start(self):
    feeling_IO.setRFIDCallback(self.rfid_id_callback)