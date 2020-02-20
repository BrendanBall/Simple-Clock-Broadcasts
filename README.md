# Simple Clock with Broadcast events
This is a fork of the SimpleMobileTools Simple Clock application. I have added broadcast events to alarms for home automation purposes, the broadcasts are called silent alarms.

A silent alarm is a non audible alarm, represented as a broadcast to the system which can be processed by other applications such as Tasker, Easer, Broadcast to mqtt or other tools for scenarios such as home automation. 

## Features
* "Silent alarms"
* "Alter, create and disable/enable alarms using intents via ADB or apps like Tasker

## Example usage scenarios
Forward broadcasts and the respective payload to an mqtt broker and have your smart home such as home assistant react to your alarms dynamically with automations such as:

- Trigger a wakeup light before the audible alarm rings
- Trigger a wakeup routine once the audible alarm rings
- Have automated actions if you use the snooze button to often :-)
- ... 

## Disclaimer

- I am not responsible, if the app messes up and you are late for anything ;) 


# Usage
Usage is generally intended with other companion apps such as Broadcast2mqtt, Tasker or Easer.

## Emitted broadcasts
Following broadcasts are emitted: 
- com.simplemobiletools.ALARM_SET : when a new Alarm is created.
- com.simplemobiletools.ALARM_GOING_TO_RING : when a silent alarm is triggered - only if the main alarm is enabled.
- com.simplemobiletools.ALARM_IS_ACTIVE when an audible alarm is active.
- com.simplemobiletools.ALARM_SNOOZED when an audible alarm has been snoozed.
- com.simplemobiletools.ALARM_DISABLED when an audible alarm has been disabled.

All broadcasts contain the same payload with the attributes:
- label
- hours  
- minutes
- days
- id

## About silent alarms
Silent alarms 

* have their own id
* have the same label as the calling alarm
* do not emit broadcasts

Configuration example using the app broadcast2mqtt
<p align="center">
<img alt="Tasker Config" src="i/broadcast2mqtt_config_example.png" width="400" />
</p>

## Alter alarms via intent
This app listens for intents sent to "android.intent.action.SEND" to the package "com.simplemobiletools.clock". The alarm id can be obtained in the UI **after** storing the alarm the first time.
<p align="center">
<img alt="Tasker Config" src="i/alarm_example.png" width="400" />
</p>
The Intent may have following payload:

Following modes options are supported: 

**INSERT**: Create new alarm

**UPDATE**: Modify existing alarm

**DISABLE**: Disable alarm

**ENABLE**: Enable alarm 

### Payloads
**MODE**: INSERT 

| Payload  | Value                       |Required |
|--------- |-----------------------------|---------|
| MODE     | (INSERT | UPDATE | DISABLE) | YES     |
| ALARM_ID | INT                         | NO      |         
| LABEL    | String                      | YES     |
| MINUTES  | INT                         | YES     |
| HOURS    | INT                         | YES     |

#### Example ADB command
    adb shell am start -a android.intent.action.SEND -t text/plain --es MODE INSERT --es LABEL "NEW_TEST_INTENT_LABEL_2" --ei "HOURS" 25 --ei "MINUTES" 65 com.simplemobiletools.clock

**MODE**: UPDATE

| Payload | Value | Required |
|---------|-------|----------|
|MODE     |UPDATE | YES      |
|ALARM_ID | INT	  | YES      |
|LABEL    | String| NO       |
|MINUTES  | INT   | NO       |
|HOURS    | INT   | NO       |

#### Example ADB command
    adb shell am start -a android.intent.action.SEND -t text/plain  --ei ALARM_ID 1 --es MODE UPDATE --es LABEL "NEW_TEST_INTENT_LABEL_3" --ei HOURS 25 --ei "MINUTES" 65 com.simplemobiletools.clock

## Example ADB Call

**MODE**: ENABLE/DISABLE
| Payload | Value          | Required |
|---------|----------------|----------|
|MODE     | ENABLE/DISABLE | YES      |
|ALARM_ID | INT	           | YES      |

##### Example ADB Call
    adb shell am start -a android.intent.action.SEND -t text/plain  --ei ALARM_ID 1 --es MODE DISABLE com.simplemobiletools.clock

## Tasker Config Example
In Tasker, create a "new intent" action in the "Tasks" section with the config as shown in the image below.

<p align="center">
<img alt="Tasker Config" src="i/tasker_config_example.png" width="400" />
</p>
# Credits
Credits go to the original creator of this app: (https://github.com/SimpleMobileTools/Simple-Clock)[https://github.com/SimpleMobileTools/Simple-Clock]