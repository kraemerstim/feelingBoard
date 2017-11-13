#!/usr/bin/env python

import RPi.GPIO as GPIO
import feeling_IO
import signal
import feeling_machine
import FB_Status
import userMode
import soundMode
import adminMode
import time

feeling_machine = feeling_machine.Feeling_Machine()
status = FB_Status.FB_Status()

# wird bei ctrl+c ausgefuehrt
def cleanup(signal,frame):
  global program_running
  print('Ctrl+C captured, ending read.')
  program_running = False

def initialize():
  global program_running
  program_running = True
  
  # hook fuer ctrl+c    
  signal.signal(signal.SIGINT, cleanup)
  
  feeling_IO.initialize()
  
  feeling_machine.addMode('user', userMode.User_Mode(status))
  feeling_machine.addMode('sound', soundMode.Sound_Mode(status))
  feeling_machine.addMode('admin', adminMode.Admin_Mode(status))
  
  status.initialize()
  feeling_machine.initialize(status)
 
def main():
  initialize()
  while program_running:
    time.sleep(0.5)
  feeling_IO.cleanup()

if __name__ == '__main__':
  main()
