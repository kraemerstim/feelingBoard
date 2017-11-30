#!/usr/bin/env python

import RPi.GPIO as GPIO
import threading
import time

BTN_1 = 26
BTN_2 = 19
BTN_3 = 13
BTN_4 = 6
BTN_5 = 5
BTN_6 = 12
HOT_BUTTON = 21

buttons = (BTN_1, BTN_2, BTN_3, BTN_4, BTN_5, BTN_6)

GPIO_EVENT = GPIO.RISING
GPIO_BOUNCETIME = 500
button_lock = threading.Lock()

def setCallbacks(aButtonCallback, aHotButtonCallback):
  global __buttonCallback
  global __hotButtonCallback
  __buttonCallback = aButtonCallback
  __hotButtonCallback = aHotButtonCallback
  
def buttonPressedCallback(channel):
  button_lock.acquire()
  try:
    time.sleep(0.02)
    if GPIO.input(channel) == 1:
      buttonPressed(channel)
  finally:
    button_lock.release()

def buttonPressed(channel):
  if channel ==  HOT_BUTTON:
    if __hotButtonCallback:
      __hotButtonCallback()
  else:
    if __buttonCallback:
      __buttonCallback(buttons.index(channel))

def initialize():
  global __buttonCallback
  global __hotButtonCallback
  __buttonCallback = None
  __hotButtonCallback = None
  
  GPIO.setmode(GPIO.BCM)
  GPIO.setup(BTN_1, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
  GPIO.setup(BTN_2, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
  GPIO.setup(BTN_3, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
  GPIO.setup(BTN_4, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
  GPIO.setup(BTN_5, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
  GPIO.setup(BTN_6, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
  GPIO.setup(HOT_BUTTON, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
  
  GPIO.add_event_detect(BTN_1, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_2, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_3, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_4, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_5, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_6, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(HOT_BUTTON, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
