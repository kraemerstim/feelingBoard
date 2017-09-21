#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import time
from datetime import datetime, time, timedelta
import RPi.GPIO as GPIO
import requests
import urllib
import netifaces as ni

#Konstanten
DebugMode = 0
SilentMode = 0
ButtonPin1 = 15
ButtonPin2 = 16
SoundPin = 18
hipchat_url = 'https://cassoftware.hipchat.com/v2/room/'

#Konfiguration
EisChannel = {'message': '(Eismann)',
              'key' : 'Hs3ZJ2kgaRsh7H1ewpzkOnNPDwztJqyk9JfCuwC5',
              'channel' : '2736569'}
    
TestButtonChannel = {'message': '(Eismann)',
              'key' : 'H4lJhYCRLI0A1LoL6DHea88dt5wDG1XCkE47tkKt',
              'channel' : '2921078'}
CustomizingChannel = {'message': 'Button Test',
              'key' : 'H4lJhYCRLI0A1LoL6DHea88dt5wDG1XCkE47tkKt',
              'channel' : '12345'}
MittagessenChannel = {'message': 'Mittagessen!!!',
              'key' : 'rxrXO1yMRe3Cx7Cdayo6JWc0gHMBl2huYshZJH9d',
              'channel' : '3902171'} 


EistimeStart = time(13, 20)
EistimeEnd = time(14, 20)

MittagessenStart = time(11, 45)
MittagessenEnd = time(13, 00)

def CallHipchatRestApi (channel):
  channelurl = hipchat_url + channel['channel'] + '/notification?auth_token=' + channel['key'] 
  params = {
      'message': channel['message'],
      'notify': 'true',
      'message_format': 'text',
      'color': 'random'
  }
  if DebugMode:  
    print(EistimeStart.isoformat() + ' ' + EistimeEnd.isoformat() + ' ' + datetime.now().time().isoformat())
    print(datetime.now().strftime('%H:%M:%S') + ' channel =' + channel + ' key = ' + key + ' message = ' + message)
    MakeGreatJobSound()
  else:
    try:
      response = requests.post(channelurl, params)
      MakeGreatJobSound()
    except:
      print('exception!!!')

def MakeGreatJobSound ():
  if not SilentMode:
    GPIO.output(SoundPin, GPIO.HIGH)
    time2.sleep(0.1)
    GPIO.output(SoundPin, GPIO.LOW)

def pressHotButton():
  channel = TestButtonChannel
  if nowTime.time() > EistimeStart and nowTime.time() < EistimeEnd:
    print('Eiszeit!!')
    channel = EisChannel
  elif nowTime.time() > MittagessenStart and nowTime.time() < MittagessenEnd:
    print('Mittagszeit!!')
    channel = MittagessenChannel
  else:
    SilentMode = 1
    print('Keine Eiszeit!!!')
  CallHipchatRestApi(channel) 
    
#initialisieren  
# RPi.GPIO Layout verwenden (wie Pin-Nummern)
GPIO.setmode(GPIO.BOARD)

# Pins fuer Button (15/16):
GPIO.setup(ButtonPin1, GPIO.IN, pull_up_down=GPIO.PUD_DOWN) #pull-down aktivieren damit bei nicht gedrückt eine 0 erkannt wird
GPIO.setup(ButtonPin2, GPIO.OUT)
GPIO.output(ButtonPin2, GPIO.HIGH)

# Pin fuer Sound (18):
GPIO.setup(SoundPin, GPIO.OUT)
GPIO.output(SoundPin, GPIO.LOW)

startTime = datetime.min

# Programm
# Dauersschleife
while 1:
  # aktuelle Zeit lesen
  nowTime = datetime.now()
  # GPIO lesen
  if GPIO.input(ButtonPin1) == GPIO.HIGH:
    if startTime == datetime.min:
      startTime = nowTime
  
        # Warte 100 ms
    time2.sleep(0.1)
  else:
    if startTime == datetime.min:
      time2.sleep(0.01)
    else:
      Previousmode = SilentMode
      durationTime = nowTime-startTime
      channel = TestButtonChannel
      if durationTime.seconds == 0:
        print('Unter einer Sekunde gedrueckt, normalmode')
        if nowTime.time() > EistimeStart and nowTime.time() < EistimeEnd:
          print('Eiszeit!!')
          channel = EisChannel
        elif nowTime.time() > MittagessenStart and nowTime.time() < MittagessenEnd:
          print('Mittagszeit!!')
          channel = MittagessenChannel
        else:
          SilentMode = 1
          print('Keine Eiszeit!!!')
        CallHipchatRestApi(channel['channel'], channel['key'], channel['message'])   
      else:
        SilentMode = 1
        print('ueber einer Sekunde gedrueckt, debugmode')
        DebugNachricht = 'Interfaces:'
        for iface in ni.interfaces():   
          ipv4s = ni.ifaddresses(iface).get(ni.AF_INET, [])
          for entry in ipv4s:
            addr = entry.get('addr')
            if not addr:
              continue
            DebugNachricht = DebugNachricht + ' ' + addr
        CallHipchatRestApi(channel['channel'], channel['key'], DebugNachricht)
      startTime = datetime.min
      SilentMode = Previousmode
      time2.sleep(1)