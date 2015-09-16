### Protocol ###


#get log
url: ip:5000/log
methods: GET
parameter: no
return: json list of logs

#login
url: ip:5000/login
methods: POST
parameter: user_id, user_password
return: token(200) or 404

#join
url: ip:5000/join
methods: POST
parameter: user_id, user_password, serialNum
return: 200 or 404

#send log
url: ip:5000/send_log
methods: POST
parameter: date, type, information, importance
return: 200 or 404(not critical)

#send video setting
url: ip:5000/setting_video
methods: POST
parameter: width, height

#door control
url : ip:5000/door
methods: POST
parameter: status(1 or 0) [1 means open // 0 means close]
return 200 or 404