#!/usr/bin/env python

import RPi.GPIO as GPIO
import signal
import feeling_machine
import threading
import time

BTN_1 = 23
BTN_2 = 5
BTN_3 = 12
BTN_4 = 6
BTN_5 = 13
BTN_6 = 25
HOT_BUTTON = 17

feelings = (BTN_1, BTN_2, BTN_3, BTN_4, BTN_5, BTN_6)

GPIO_EVENT = GPIO.RISING
GPIO_BOUNCETIME = 500
button_lock = threading.Lock()

feeling_machine = feeling_machine.Feeling_Machine()

# wird bei ctrl+c ausgefuehrt
def cleanup(signal,frame):
  global program_running
  print('Ctrl+C captured, ending read.')
  program_running = False

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
    feeling_machine.hot_button_pressed()
  else:
    feeling_machine.button_pressed(feelings.index(channel))

def initialize():
  global program_running
  program_running = True
  
  # hook fuer ctrl+c    
  signal.signal(signal.SIGINT, cleanup)
  
  GPIO.setmode(GPIO.BCM)

  feeling_machine.initialize()
    
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
 
def main():
  print('0')
  initialize()
  print('1')
  while program_running:
    time.sleep(0.5)
  print('2')
  feeling_machine.cleanup()

if __name__ == '__main__':
  main()
