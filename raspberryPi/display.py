#!/usr/bin/python

#import
import lcd
import threading
import time
from threading import Thread

class Display:

	def setDisplay(self, line1, line2, seconds=0, style=2):
		print('input = %(line1)s, %(line2)s, displaynumber = %(displaynumber)d, seconds = %(seconds)d' % {'line1': line1, 'line2': line2, 'displaynumber': self.display_number, 'seconds': seconds})
		self.lcd_lock.acquire()
		try:
			lcd.lcd_display(line1, line2, style)
			if seconds > 0:
				self.display_number += 1
				print('input = %(line1)s, %(line2)s, displaynumber = %(displaynumber)d.' % {'line1': line1, 'line2': line2, 'displaynumber': self.display_number})
				Thread(target=self.__threaded_display_method, args=(self.display_number, seconds)).start()
		finally:
			self.lcd_lock.release()

	def __threaded_display_method(self, displaynumber, seconds):
		time.sleep(seconds)
		if self.display_number == displaynumber:
			print('closing displaynumber = %(displaynumber)d.' % {'displaynumber': self.display_number})
			self.setDisplay(self.head_line, self.bottom_line)
	
	def __init__(self):
		self.lcd_lock = threading.Lock()
		self.display_number = 0
		self.head_line = "Gefuehlsboard"
		self.bottom_line = "2.0"
		lcd.lcd_init()
	
	def cleanup(self):
		lcd.GPIO.cleanup()