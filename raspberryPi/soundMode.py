#!/usr/bin/env python
# -*- coding: utf8 -*-

import FB_Status
import feeling_rest
from datetime import datetime, timedelta
from pygame import mixer
import os
from random import randint

class Sound_Mode:
  
  def __init__(self, aStatus, aDisplay):
    self.status = aStatus
    self.display = aDisplay
    
    mixer.init()
    self.sounds = []
    self.sounds.append('sounds/weird_russian.mp3')
    self.sounds.append('sounds/letitgo.mp3')
    self.sounds.append('sounds/timetogetlaid.mp3')
    self.sounds.append('sounds/justhadsex.mp3')
    self.sounds.append('sounds/incrediblethoughts.mp3')
    
    self.randomSounds = []
    for root, dirs, files in os.walk('sounds/random/'):
      for filename in files:
        self.randomSounds.append('sounds/random/' + filename)
  
  def ButtonPressed(self, button):
    if (button < len(self.sounds)):
      sound = self.sounds[button]
    else:
      sound = self.randomSounds[randint(0, len(self.randomSounds)-1)]
      
    mixer.music.load(sound)
    self.display.setDisplay('Now Playing', sound)
    mixer.music.play()
  
  def HotButtonPressed(self):
    self.status.resetMode()