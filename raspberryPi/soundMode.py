#!/usr/bin/env python
# -*- coding: utf8 -*-

import FB_Status
import feeling_rest
from datetime import datetime, timedelta
from pygame import mixer
import os
from random import randint

class Sound_Mode:
  
  def getFilePath(aFilePath):
    return os.path.join(os.path.dirname(__file__), aFilePath)
  
  def __init__(self, aStatus, aDisplay):
    self.status = aStatus
    self.display = aDisplay
    
    mixer.init()
    self.sounds = []
    self.sounds.append(Sound_Mode.getFilePath('sounds/weird_russian.mp3'))
    self.sounds.append(Sound_Mode.getFilePath('sounds/letitgo.mp3'))
    self.sounds.append(Sound_Mode.getFilePath('sounds/timetogetlaid.mp3'))
    self.sounds.append(Sound_Mode.getFilePath('sounds/justhadsex.mp3'))
    self.sounds.append(Sound_Mode.getFilePath('sounds/incrediblethoughts.mp3'))
    
    self.randomSounds = []
    for root, dirs, files in os.walk(Sound_Mode.getFilePath('sounds/random/')):
      for filename in files:
        self.randomSounds.append(Sound_Mode.getFilePath('sounds/random/' + filename))
  
  def ButtonPressed(self, button):
    if (button < len(self.sounds)):
      sound = self.sounds[button]
    else:
      sound = self.randomSounds[randint(0, len(self.randomSounds)-1)]
      
    mixer.music.load(sound)
    self.display.setDisplay('Now Playing', os.path.basename(sound))
    mixer.music.play()
  
  def HotButtonPressed(self):
    self.status.resetMode()