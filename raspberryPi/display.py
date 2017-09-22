#!/usr/bin/python

#import
import lcd
import threading
import time
from threading import Thread

class Display:

  def setDisplay(self, line1, line2, seconds=0, style=2):
    self.display_number, 'seconds': seconds})
    self.lcd_lock.acquire()
    try:
      lcd.lcd_display(line1, line2, style)
      self.display_number += 1
      if seconds > 0:
        Thread(target=self.__threaded_display_reset, args=(self.display_number, seconds)).start()
    finally:
      self.lcd_lock.release()

  def __threaded_display_reset(self, displaynumber, seconds):
    time.sleep(seconds)
    if self.display_number == displaynumber:
      self.setDisplay(self.head_line, self.bottom_line)
  
  def __threaded_display_move(self, displaynumber, line1, line2, interval = 0.5)
    iteration = 0
    while (self.display_number == displaynumber)
      topLine = __get_line_part(line1, iteration)
      bottomLine = __get_line_part(line2, iteration)
      
      time.sleep(interval)
      iteration += 1
  
  def __get_line_part(line, iteration):
    scount = len(line)
    if scount <= 16:
      return line
    
    step = iteration % (scount - 15)
    step += 4 #hinzufügen der start und end-pausen
    start -= 2 #start berechnen
    if (start <= 0):
      start = 0
      
    if start > (scount - 15):
      start = (scount - 15)
    
    return line[start:]
  
  def set_default_values(self, line1, line2):
    self.head_line = line1
    self.bottom_line = line2
    
  def __init__(self):
    self.lcd_lock = threading.Lock()
    self.display_number = 0
    self.head_line = "Gefuehlsboard"
    self.bottom_line = "2.0"
    lcd.lcd_init()
  
  def cleanup(self):
    lcd.GPIO.cleanup()