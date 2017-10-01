#!/usr/bin/env python
# -*- coding: utf8 -*-

import FB_Status
import feeling_rest
from datetime import datetime, timedelta
from pygame import mixer

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
  
  def ButtonPressed(self, button):
    mixer.music.load(self.sounds[channel])
    mixer.music.play()
    self.display.setDisplay('Now Playing', self.sounds[channel])
    
  
  def HotButtonPresses(self):
    pass