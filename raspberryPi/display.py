#!/usr/bin/python

#import
import lcd
import threading

lcd_lock = threading.Lock()

def set(line1, line2, style=2):
	lcd_lock.acquire()
	try:
		lcd.lcd_display(line1, line2, style)
	finally:
		lcd_lock.release()

def cleanup():
	lcd.GPIO.cleanup()

def init():
	lcd.lcd_init()