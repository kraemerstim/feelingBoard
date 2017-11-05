#!/usr/bin/env python

class Feeling_Machine:
  def __init__(self):
    self.modes_dict = {}
    
  def getCurrentModeObject(self):
    if self.status.Mode in self.modes_dict:
      return self.modes_dict[self.status.Mode]
  
  def hot_button_pressed(self):
    mode_object = self.getCurrentModeObject()
    if mode_object != None:
      mode_object.HotButtonPressed()
  
  def button_pressed(self, channel):
    mode_object = self.getCurrentModeObject()
    if mode_object != None:
      mode_object.ButtonPressed(channel)

  def statusUserCallback(self, uidString, userName):
    mode_object = self.getCurrentModeObject()
    if mode_object != None:
      mode_object.userChanged(uidString, userName)
      
  def statusModeCallback(self, mode):
    mode_object = self.getCurrentModeObject()
    if mode_object != None:
      mode_object.modeChanged()
    
  def addMode(self, modeString, modeObject):
    if modeString not in self.modes_dict:
      self.modes_dict[modeString] = modeObject
  
  def initialize(self, aStatus):
    self.status = aStatus
    
    self.status.start(self.statusUserCallback, self.statusModeCallback)
    
    self.statusModeCallback('User')
