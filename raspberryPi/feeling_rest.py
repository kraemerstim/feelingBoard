#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from datetime import datetime, time, timedelta
import requests

#Konstanten
hipchat_url = 'https://cassoftware.hipchat.com/v2/room/'
feeling_board_url = 'http://localhost:8080/'

#Konfiguration
EisChannel = {'message': '(Eismann)',
              'key' : 'Hs3ZJ2kgaRsh7H1ewpzkOnNPDwztJqyk9JfCuwC5',
              'channel' : '2736569'}
TestButtonChannel = {'message': '(Eismann)',
              'key' : 'H4lJhYCRLI0A1LoL6DHea88dt5wDG1XCkE47tkKt',
              'channel' : '2921078'}
MittagessenChannel = {'message': 'Mittagessen!!!',
              'key' : 'rxrXO1yMRe3Cx7Cdayo6JWc0gHMBl2huYshZJH9d',
              'channel' : '3902171'} 


EistimeStart = time(13, 20)
EistimeEnd = time(14, 20)

MittagessenStart = time(11, 45)
MittagessenEnd = time(13, 0)

def CallHipchatRestApi (channel):
  channelurl = hipchat_url + channel['channel'] + '/notification?auth_token=' + channel['key'] 
  params = {'message': channel['message'],
            'notify': 'true',
            'message_format': 'text',
            'color': 'random'
  }
  response = requests.post(channelurl, params, timeout=3)
            
def getAccountByUid(uid):
  response = None
  account_url = feeling_board_url + 'account'
  response = requests.get(account_url + '/chipid/' + uid, timeout = 5)
  if not response.text:
    params = {'chipUID': uid}
    response = requests.post(account_url, json=params, timeout=5)
    response = requests.get(response.headers['location'])
  return response

def addFeelingBoardEntry(rfid_uid, feeling):
  accountID = 0
  account = getAccountByUid(rfid_uid)
  if account and account.json():
    accountID = account.json()['id']
  else:
    return True
    
  feeling_url = feeling_board_url + 'entry'
  params = {'accountID': accountID, 'feeling': feeling}
  response = requests.post(feeling_url, json=params, timeout=5)
  if (response and response.ok and response.headers['location']):
    return False
    
  return True

def callHipchatApi():
  nowTime = datetime.now()
  channel = TestButtonChannel
  if nowTime.time() > EistimeStart and nowTime.time() < EistimeEnd:
    channel = EisChannel
  elif nowTime.time() > MittagessenStart and nowTime.time() < MittagessenEnd:
    channel = MittagessenChannel
  CallHipchatRestApi(channel) 

