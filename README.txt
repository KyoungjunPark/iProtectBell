### Protocol ###


#get log

#url: ip:5000/log
#methods: GET
#parameter: no
#return: json list of logs

#login

#url: ip:5000/login
#methods: POST
#parameter: user_id, user_password
#return: token(200) or 404

#join

#url: ip:5000/join
#methods: POST
#parameter: user_id, user_password, serialNum
#return: 200 or 404

#send log

#url: ip:5000/send_log
#methods: POST
#parameter: date, information, importance
#return: 200 or 404(not critical)