#!/usr/bin/env python

import RPi.GPIO as GPIO
import rfidWrapper
import display
import signal
import time
import threading
from threading import Thread
import feeling_rest
from datetime import datetime, timedelta
import traceback
from pygame import mixer

continue_reading = True

BTN_1 = 23
BTN_2 = 5
BTN_3 = 12
BTN_4 = 6
BTN_5 = 13
BTN_6 = 25

LED_1 = 20

GREAT_JOB_SOUND = 24
HOT_BUTTON = 17

SOUND_CARD = "33177163213230"

GPIO_EVENT = GPIO.RISING
GPIO_BOUNCETIME = 500
RFID_HOLD_TIME = 1

BUTTON_REPEAT_DELAY = timedelta(seconds=5)
Button_Activate_Time = datetime.min

button_lock = threading.Lock()

feelings = {BTN_1:1, BTN_2:2, BTN_3:3, BTN_4:4, BTN_5:5, BTN_6:6}

RFID_uid = "0"
RFID_name = "Anton"

# wird bei ctrl+c ausgefuehrt
def end_read(signal,frame):
    global continue_reading
    print('Ctrl+C captured, ending read.')
    continue_reading = False

def makeGreatJobSound (channel):
    time.sleep(0.02)
    if GPIO.input(channel) == 1:
        GPIO.output(GREAT_JOB_SOUND, GPIO.HIGH)
        time.sleep(0.1)
        GPIO.output(GREAT_JOB_SOUND, GPIO.LOW)

def testSoundButton (channel):
    global sounds
    time.sleep(0.02)
    if GPIO.input(channel) == 1:
        mixer.music.load(sounds[feelings[channel]-1])
        mixer.music.play()
        display.set('Now Playing', sounds[feelings[channel]-1])
        
        
# hook fuer ctrl+c    
signal.signal(signal.SIGINT, end_read)

RFIDReader = rfidWrapper.RFID_Wrapper()

def buttonPressedCallback(channel):
    global Button_Activate_Time
    button_lock.acquire()
    try:
        time.sleep(0.02)
        if datetime.now() > Button_Activate_Time and GPIO.input(channel) == 1:
            buttonPressed(channel)
    finally:
        button_lock.release()

def buttonPressed(channel):
    global Button_Activate_Time
    if sound_mode:
        testSoundButton(channel)
    else:
        feeling = feelings[channel]
        try:
            response = feeling_rest.addFeelingBoardEntry(RFID_uid, feeling)
            if (response and response.ok and response.headers['location']):
                feeling_rest.getEntryInfo(response.headers['location'])
            Button_Activate_Time = datetime.now() + BUTTON_REPEAT_DELAY
            display.set('Danke ' + RFID_name + ' ' + str(feeling), 'fuers Mitmachen!')
        except Exception as e:
            display.set('Exception bei', 'addFeelingBoardEntry')
            Button_Activate_Time = datetime.now() + BUTTON_REPEAT_DELAY
            traceback.print_exc()

def rfid_id_callback(uidString):
    global last_RFID_found_timestamp
    nowTime = datetime.now()
    if (uidString != '0'):
        last_RFID_found_timestamp = datetime.now()
        setChipUID(uidString)
    else:
        durationTime = nowTime - last_RFID_found_timestamp
        if durationTime.seconds >= RFID_HOLD_TIME:
            setChipUID(uidString)
            last_RFID_found_timestamp = datetime.min
        else:
            setChipUID(RFID_uid)
    
    
def initialize():
    global last_RFID_found_timestamp
    global sounds
    global sound_mode
    sound_mode = 0
    last_RFID_found_timestamp = datetime.min
    
    GPIO.setmode(GPIO.BCM)

    GPIO.setup(LED_1, GPIO.OUT)   
    GPIO.setup(BTN_1, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
    GPIO.setup(BTN_2, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
    GPIO.setup(BTN_3, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
    GPIO.setup(BTN_4, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
    GPIO.setup(BTN_5, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)
    GPIO.setup(BTN_6, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)

    GPIO.setup(GREAT_JOB_SOUND, GPIO.OUT)   
    GPIO.setup(HOT_BUTTON, GPIO.IN, pull_up_down = GPIO.PUD_DOWN)

    display.init()
    
    GPIO.add_event_detect(BTN_1, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
    GPIO.add_event_detect(BTN_2, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
    GPIO.add_event_detect(BTN_3, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
    GPIO.add_event_detect(BTN_4, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
    GPIO.add_event_detect(BTN_5, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)
    GPIO.add_event_detect(BTN_6, GPIO_EVENT, callback=buttonPressedCallback, bouncetime=GPIO_BOUNCETIME)

    GPIO.add_event_detect(HOT_BUTTON, GPIO_EVENT, callback=makeGreatJobSound, bouncetime=GPIO_BOUNCETIME)
    
    mixer.init()
    sounds = []
    sounds.append('sounds/weird_russian.mp3')
    sounds.append('sounds/letitgo.mp3')
    sounds.append('sounds/timetogetlaid.mp3')
    sounds.append('sounds/justhadsex.mp3')
    sounds.append('sounds/incrediblethoughts.mp3')
    
    RFIDReader.start(rfid_id_callback)

def setChipUID(uid):
    global RFID_uid
    global RFID_name
    if (RFID_uid != uid):
        RFID_uid = uid
        try:
            RFID_name = feeling_rest.getUserNameByUid(RFID_uid)
        except:
            RFID_name = 'Error'
        if not RFID_name:
            RFID_name = 'Anton'
        rfidUidChanged()

def rfidUidChanged():
    global sound_mode
    if (RFID_uid == '0'):
        GPIO.output(LED_1, GPIO.LOW)
        display.set('Gefuehlsboard', '2.0')
    elif RFID_uid == SOUND_CARD:
        sound_mode = 1
    else:
        sound_mode = 0
        GPIO.output(LED_1, GPIO.HIGH)
        display.set('Hallo ' + RFID_name, 'Wie geht\'s?')
          
def main():
    global Button_Activate_Time
    initialize()
    while continue_reading:
        if (Button_Activate_Time != datetime.min and Button_Activate_Time < datetime.now()):
            Button_Activate_Time = datetime.min
            rfidUidChanged()
        time.sleep(0.5)
    display.set('Bye bye', ':(')
    RFIDReader.stop()
    display.cleanup()

if __name__ == '__main__':
  main()
