#!/usr/bin/python

#import
import lcd
import threading
import time
from threading import Thread

class Display:

  def __get_line_part(line, iteration):
    scount = len(line)
    if scount <= 16:
      return line
    
    step = iteration % (scount-16+10)
    start = step - 5 #start berechnen
    if (start <= 0):
      start = 0
      
    if start > (scount - 16):
      start = (scount - 16)
    
    return line[start:(start + 16)]
    
  def setDisplay(self, line1, line2, seconds=0, style=2):
    self.lcd_lock.acquire()
    self.display_number += 1
    try:
      if len(line1) <= 16 and len(line2) <= 16:
        lcd.lcd_display(line1, line2, style)
      else:
        Thread(target=self.__threaded_display_move, args=(self.display_number, line1, line2)).start()
      if seconds > 0:
        Thread(target=self.__threaded_display_reset, args=(self.display_number, seconds)).start()
    finally:
      self.lcd_lock.release()

  def __threaded_display_reset(self, displaynumber, seconds):
    time.sleep(seconds)
    if self.display_number == displaynumber:
      self.setDisplay(self.head_line, self.bottom_line)
  
  def __threaded_display_move(self, displaynumber, line1, line2, interval=0.2):
    iteration = 0
    while (self.display_number == displaynumber):
      topLine = Display.__get_line_part(line1, iteration)
      bottomLine = Display.__get_line_part(line2, iteration)
      iteration += 1
      print (topLine + " " + bottomLine + " " + str(iteration) + " " + str(interval))
      lcd.lcd_display(topLine, bottomLine,2)
      time.sleep(interval)
  
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