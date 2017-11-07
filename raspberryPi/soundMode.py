#!/usr/bin/env python
# -*- coding: utf8 -*-

import FB_Status
import feeling_rest
import feeling_IO
from datetime import datetime, timedelta
import os
from random import randint

class Sound_Mode:
  
  def getFilePath(aFilePath):
    return os.path.join(os.path.dirname(__file__), aFilePath)
    
  def getFileName(aFilePath):
    filename = os.path.basename(aFilePath)
    return filename[:-4]
  
  def __init__(self, aStatus):
    self.status = aStatus
    
    self.sounds = []
    self.sounds.append(Sound_Mode.getFilePath('sounds/weird_russian.mp3'))
    self.sounds.append(Sound_Mode.getFilePath('sounds/letitgo.mp3'))
    self.sounds.append(Sound_Mode.getFilePath('sounds/timetogetlaid.mp3'))
    self.sounds.append(Sound_Mode.getFilePath('sounds/justhadsex.mp3'))
    self.sounds.append(Sound_Mode.getFilePath('sounds/incrediblethoughts.mp3'))
    
    self.randomSounds = []
    for root, dirs, files in os.walk(Sound_Mode.getFilePath('sounds/random/')):
      for filename in files:
        if filename.endswith('.mp3'):
          self.randomSounds.append(Sound_Mode.getFilePath('sounds/random/' + filename))
  
  def ButtonPressed(self, button):
    if (button < len(self.sounds)):
      sound = self.sounds[button]
    else:
      sound = self.randomSounds[randint(0, len(self.randomSounds)-1)]
      
    feeling_IO.playSound(sound)
    feeling_IO.setDisplay('Now Playing', Sound_Mode.getFileName(sound))
  
  def HotButtonPressed(self):
    self.status.resetMode()
    
  def userChanged(self, aUid, aUserName):
    pass
    
  def modeChanged(self):
    feeling_IO.setDefaultDisplayValues('Sound Mode', 'Chose your sound')
    feeling_IO.setDisplay('Sound Mode', 'Chose your sound')