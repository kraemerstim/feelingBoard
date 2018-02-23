#!/usr/bin/env python

import threading
import time
import RPi.GPIO as GPIO

#Buttons(board): 37,35,33,31,29,18,32
#Buttons(bcm): 26,19,13,6,5,24,12

#HotButton(board): 3 oder 5
#HotButton(bcm): 2 oder 3
BTN_1 = 26
BTN_2 = 19
BTN_3 = 13
BTN_4 = 6
BTN_5 = 5
BTN_6 = 24
BTN_7 = 12
HOT_BUTTON = 2

buttons = (BTN_1, BTN_2, BTN_4, BTN_5)

GPIO_EVENT = GPIO.FALLING
GPIO_PULL_UP_DOWN = GPIO.PUD_UP
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
  GPIO.setup(BTN_1, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_2, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_3, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_4, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_5, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_6, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_7, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(HOT_BUTTON, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
  
  GPIO.add_event_detect(BTN_1, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_2, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_3, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_4, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_5, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_6, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_7, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(HOT_BUTTON, GPIO.RISING, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)

def debug(channel):
  global program_running
  print("Button pressed: " + str(channel))
  if channel == HOT_BUTTON:
    program_running = False

def main():
  global program_running
  GPIO.setmode(GPIO.BCM)
  GPIO.setup(BTN_1, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_2, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_3, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_4, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_5, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_6, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(BTN_7, GPIO.IN, pull_up_down = GPIO_PULL_UP_DOWN)
  GPIO.setup(HOT_BUTTON, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)

  GPIO.add_event_detect(BTN_1, GPIO_EVENT, callback=debug, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_2, GPIO_EVENT, callback=debug, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_3, GPIO_EVENT, callback=debug, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_4, GPIO_EVENT, callback=debug, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_5, GPIO_EVENT, callback=debug, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_6, GPIO_EVENT, callback=debug, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(BTN_7, GPIO_EVENT, callback=debug, bouncetime=GPIO_BOUNCETIME)
  GPIO.add_event_detect(HOT_BUTTON, GPIO_EVENT, callback=debug, bouncetime=GPIO_BOUNCETIME)
  
  while program_running:
    time.sleep(0.5)
  GPIO.cleanup()

if __name__ == '__main__':
  main()