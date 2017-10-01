#!/usr/bin/env python

import feeling_rest
import rfidWrapper

class FB_Status:
    
  def initialize(self):
    self.RFID_uid = '0'
    self.RFID_name = 'Anton'
    self.RFID_role = 'User'
    self.Mode = 'User'

  def rfid_id_callback(self, uidString):
    if (self.RFID_uid != uidString):
      self.RFID_uid = uidString;
      try:
        account = feeling_rest.getAccountByUid(self.RFID_uid)
        if account:
          name = account.json()['name']
          role = account.json()['role']
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
      self.Mode = self.RFID_role
      print('Mode = ' + self.Mode)
      
    self.__callback(self.RFID_uid, self.RFID_name, self.RFID_role)
  
  def start(self, callback):
    self.__callback = callback
    self.RFIDReader = rfidWrapper.RFID_Wrapper()
    self.RFIDReader.start(self.rfid_id_callback)
        
  def cleanup(self):
    self.RFIDReader.stop()