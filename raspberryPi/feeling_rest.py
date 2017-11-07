#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from datetime import datetime, time, timedelta
import requests
import feeling_IO

#Konstanten
hipchat_url = 'https://cassoftware.hipchat.com/v2/room/'
feeling_board_url = 'http://localhost:8080/rest/'

EistimeStart = time(13, 20)
EistimeEnd = time(14, 20)

MittagessenStart = time(11, 45)
MittagessenEnd = time(13, 0)

def CallHipchatRestApi (channel):
  lChannelUrl = hipchat_url + feeling_IO.getIniValue(channel, 'channel') + '/notification?auth_token=' + feeling_IO.getIniValue(channel, 'key')  
  lParams = {'message': feeling_IO.getIniValue(channel, 'message'),
            'notify': 'true',
            'message_format': 'text',
            'color': 'random'
  }
  response = requests.post(lChannelUrl, lParams, timeout=3)
            
def getAccountByUid(uid):
  restKey = feeling_IO.getIniValue('Security', 'RestKey')
  response = None
  account_url = feeling_board_url + 'account'
  header = {'Key': restKey}
  response = requests.get(account_url + '/chipid/' + uid, headers=header, timeout = 5)
  if not response.text:
    params = {'chipUID': uid}
    response = requests.post(account_url, json=params, timeout=5)
    response = requests.get(response.headers['location'])
  return response

def addFeelingBoardEntry(rfid_uid, feeling):
  restKey = feeling_IO.getIniValue('Security', 'RestKey')
  accountID = 0
  account = getAccountByUid(rfid_uid)
  if account and account.json():
    accountID = account.json()['id']
  else:
    return True
    
  feeling_url = feeling_board_url + 'entry'
  params = {'accountID': accountID, 'feeling': feeling}
  header = {'Key': restKey}
  response = requests.post(feeling_url, json=params, headers=header, timeout=5)
  if (response and response.ok and response.headers['location']):
    return False
  return True

def callHipchatApi():
  nowTime = datetime.now()
  channel = 'DEFAULT'
  if testmode:
    channel = 'DEFAULT'
  elif nowTime.time() > EistimeStart and nowTime.time() < EistimeEnd:
    channel = 'EisChannel'
  elif nowTime.time() > MittagessenStart and nowTime.time() < MittagessenEnd:
    channel = 'MittagessenChannel'
  CallHipchatRestApi(channel) 