#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from datetime import datetime, time
import requests
import feeling_IO

EistimeStart = time(13, 20)
EistimeEnd = time(14, 20)

MittagessenStart = time(11, 45)
MittagessenEnd = time(13, 0)

def CallHipchatRestApi (channel):
  lChannelUrl = feeling_IO.getIniValue('Hipchat', 'url') + feeling_IO.getIniValue(channel, 'channel') + '/notification?auth_token=' + feeling_IO.getIniValue(channel, 'key')  
  lParams = {'message': feeling_IO.getIniValue(channel, 'message'),
            'notify': 'true',
            'message_format': 'text',
            'color': 'random'
  }
  response = requests.post(lChannelUrl, lParams, timeout=3)

def getAccountByUid(uid, getNewCode=False):
  restKey = feeling_IO.getIniValue('Security', 'RestKey')
  response = None
  account_url = feeling_IO.getIniValue('FeelingWebservice', 'url') + 'account'
  header = {'Key': restKey}
  response = requests.get(account_url + '/chipid/' + uid, headers=header, timeout = 5)
  if not response.text:
    params = {'chipUID': uid}
    response = requests.post(account_url, headers=header, json=params, timeout=5)
  if getNewCode:
    requests.put(account_url + '/chipid/' + uid, headers=header, timeout=5)
  response = requests.get(account_url + '/chipid/' + uid, headers=header, timeout = 5)
  print(response.text)
  return response

def addFeelingBoardEntry(rfid_uid, feeling):
  restKey = feeling_IO.getIniValue('Security', 'RestKey')
  accountID = 0
  account = getAccountByUid(rfid_uid)
  if account and account.json():
    accountID = account.json()['id']
  else:
    return (True, '')
    
  feeling_url = feeling_IO.getIniValue('FeelingWebservice', 'url') + 'entry'
  params = {'accountID': accountID, 'feeling': feeling}
  header = {'Key': restKey}
  response = requests.post(feeling_url, json=params, headers=header, timeout=5)
  if (response and response.ok and 'location' in response.headers):
    if ('achievement' in response.headers):
      return (False, response.headers['achievement'])
    else:
      return (False, '')
  return (True, '')

def callHipchatApi():
  nowTime = datetime.now()
  channel = 'DEFAULT'
  if nowTime.time() > EistimeStart and nowTime.time() < EistimeEnd:
    channel = 'EisChannel'
    feeling_IO.makeGreatJobSound()
  elif nowTime.time() > MittagessenStart and nowTime.time() < MittagessenEnd:
    channel = 'MittagessenChannel'
    feeling_IO.playSound('sounds/mittagessen.mp3')
  CallHipchatRestApi(channel) 
