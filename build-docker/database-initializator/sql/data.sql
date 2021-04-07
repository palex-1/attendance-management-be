set search_path to attendance_management;

-- VERSION 1.0.0 QUERIES START
INSERT INTO SUPPORTED_LANG_I18N(LANG) 
VALUES('IT');

INSERT INTO MESSAGE_TYPE(M_TYPE) VALUES  
('ONE_TIME_PSW_SMS');

INSERT INTO MESSAGE_TEMPLATE(M_TYPE, LANG, SUBJECT, MESSAGE)
VALUES
('ONE_TIME_PSW_SMS', 'IT', 'Codice di Verifica', 'Time Sheet. Il tuo codice di verifica è ${one_time_psw_placeholder}');


INSERT INTO MESSAGE_TYPE(M_TYPE) VALUES  
('PASSWORD_CHANGE_COMMUNICATION_EMAIL'),
('USER_INVITED_TO_APP'),
('RESET_PSW_EMAIL');


INSERT INTO MESSAGE_TEMPLATE(M_TYPE, LANG, SUBJECT, MESSAGE)
VALUES
('USER_INVITED_TO_APP', 'IT', 'Benvenuto/a', '<!doctype html> <html> <head> <meta name="viewport" content="width=device-width" /> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> <title>${mail_subject}</title> <style> /* ------------------------------------- GLOBAL RESETS ------------------------------------- */ /*All the styling goes here*/ img { border: none; -ms-interpolation-mode: bicubic; max-width: 100%; } body { background-color: #f6f6f6; font-family: Helvetica, Arial, serif; -webkit-font-smoothing: antialiased; font-size: 14px; line-height: 1.4; margin: 0; padding: 0; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; } table { border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; } table td { font-family: Helvetica, Arial, serif; font-size: 14px; vertical-align: top; } /* ------------------------------------- BODY & CONTAINER ------------------------------------- */ .body { background-color: #f6f6f6; width: 100%; } /* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */ .container { display: block; margin: 0 auto !important; /* makes it centered */ max-width: 580px; padding: 10px; width: 580px; } @media screen and (max-width: 580px) { .container { width: 100% !important; max-width: 100% !important; min-width: 320px !important; } .content { max-width: 100% !important; } } /* ------------------------------------- HEADER, FOOTER, MAIN ------------------------------------- */ .wrapper { box-sizing: border-box; padding: 20px; } .footer { clear: both; margin-top: 10px; text-align: center; width: 100%; } .footer td, .footer p, .footer span, .footer a { color: #999999; font-size: 12px; text-align: center; } /* ------------------------------------- TYPOGRAPHY ------------------------------------- */ h1, h2, h3, h4 { color: #000000; font-family: Helvetica, Arial, serif; font-weight: 400; line-height: 1.4; margin: 0; margin-bottom: 10px; } h1 { font-size: 35px; font-weight: 300; text-align: center; text-transform: capitalize; } p, ul, ol, pre { font-family: Helvetica, Arial, serif; font-size: 14px; font-weight: normal; margin: 0; margin-bottom: 15px; } p li, ul li, ol li { list-style-position: inside; margin-left: 5px; } a { color: #1687e8; text-decoration: underline; } /* ------------------------------------- BUTTONS ------------------------------------- */ .btn { box-sizing: border-box; width: 100%; } .btn>tbody>tr>td { padding-bottom: 15px; } .btn table { width: auto; } .btn table td { background-color: #ffffff; border-radius: 5px; text-align: center; } .btn a { background-color: #ffffff; border: solid 1px #1687e8; border-radius: 5px; box-sizing: border-box; color: #1687e8; cursor: pointer; display: inline-block; font-size: 14px; font-weight: bold; margin: 0; padding: 12px 25px; text-decoration: none; text-transform: capitalize; } .btn-primary table td { background-color: #1687e8; } .btn-primary a { background-color: #1687e8; border-color: #1687e8; color: #ffffff; } /* ------------------------------------- OTHER STYLES THAT MIGHT BE USEFUL ------------------------------------- */ .last { margin-bottom: 0; } .first { margin-top: 0; } .align-center { text-align: center; } .align-right { text-align: right; } .align-left { text-align: left; } .clear { clear: both; } .mt0 { margin-top: 0; } .mb0 { margin-bottom: 0; } .preheader { color: transparent; display: none; height: 0; max-height: 0; max-width: 0; opacity: 0; overflow: hidden; mso-hide: all; visibility: hidden; width: 0; } .powered-by a { text-decoration: none; } hr { border: 0; border-bottom: 1px solid #f6f6f6; margin: 20px 0; } /* ------------------------------------- RESPONSIVE AND MOBILE FRIENDLY STYLES ------------------------------------- */ @media only screen and (max-width: 620px) { table[class=body] h1 { font-size: 28px !important; margin-bottom: 10px !important; } table[class=body] p, table[class=body] ul, table[class=body] ol, table[class=body] td, table[class=body] span, table[class=body] a { font-size: 16px !important; } table[class=body] .wrapper, table[class=body] .article { padding: 10px !important; } table[class=body] .content { padding: 0 !important; } table[class=body] .container { padding: 0 !important; width: 100% !important; } table[class=body] .main { border-left-width: 0 !important; border-radius: 0 !important; border-right-width: 0 !important; margin-top: 5px; } table[class=body] .btn table { width: 100% !important; } table[class=body] .btn a { width: 100% !important; } table[class=body] .img-responsive { height: auto !important; max-width: 100% !important; width: auto !important; } small { font-size: 10px; } } /* ------------------------------------- PRESERVE THESE STYLES IN THE HEAD ------------------------------------- */ @media all { .ExternalClass { width: 100%; } .ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div { line-height: 100%; } .apple-link a { color: inherit !important; font-family: inherit !important; font-size: inherit !important; font-weight: inherit !important; line-height: inherit !important; text-decoration: none !important; } .btn-primary table td:hover { background-color: #1687e8 !important; } .btn-primary a:hover { background-color: #1687e8 !important; border-color: #1687e8 !important; } } </style> </head> <body class=""> <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="background-color: #f6f6f6; width: 100%;"> <tr> <td>&nbsp;</td> <td class="container" style="display: block; margin: 0 auto !important; max-width: 580px; padding: 0px; width: 580px; "> <div class="content" style="box-sizing: border-box; display: block; margin: 0 auto; max-width: 580px; padding: 15px;"> <!-- START CENTERED WHITE CONTAINER --> <table role="presentation" class="main" style="background: #ffffff; border-radius: 3px; width: 100%; "> <!-- START MAIN CONTENT AREA --> <tr> <td class="wrapper" > <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="width: 100%; color: white !important;"> <tr> <td style="padding: 20px;"> <div style="text-align: center;"> <img width="200" src="cid:companyLogo" /> </div> <hr id="title-hr" style="background-color:#1687e8; height:2px; margin: 20px 0; margin-top:0px;"> <p style="text-align: center"> <h2 style="display: block; text-align: center; font-weight: bold;"> ${mail_subject} </h2> <h4 style="display: block;text-align: center;"> <span style="display: block; text-align: center;"> Sei stato aggiunto all''applicativo Time Sheet. </span> </h4> <h4 style="display: block; text-align: center;"> <b>Credenziali:</b> <span style="display: block; margin-left: 15px;"> Username: <i>${username}</i> </span> <span style="display: block; margin-left: 15px;"> Password: <i>${password}</i> </span> </h4> <br> <span style="display: block;text-align: center; margin-bottom:20px;"> <a href="${frontend_link}">Collegati</a> </span> <h4 style="text-align: center;"> <span style="display: block; text-align: center; font-size: smaller;"> Il Team di Time Sheet </span> </p> </td> </tr> </table> </td> </tr> <!-- END MAIN CONTENT AREA --> </table> <!-- START FOOTER --> <div class="footer" style="text-align: center;"> <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="width: 100%; text-align: center;"> <tr> <td style="padding-bottom: 10px; padding-top: 10px; color: #999999;"> <small style="color: #999999; font-size: 12px; text-align: center; display: block; margin-bottom: 5px;"> Mail inviata in automatico, si prega di non rispondere.<br> </small> <small style="color: #999999; font-size: 12px; text-align: center; "> Copyright © 2018 Time Sheet. </small> </td> </tr> <tr> </tr> </table> </div> <!-- END FOOTER --> <!-- END CENTERED WHITE CONTAINER --> </div> </td> <td>&nbsp;</td> </tr> </table> </body> </html>'),
('PASSWORD_CHANGE_COMMUNICATION_EMAIL', 'IT', 'Comunicazione cambio password','<!doctype html> <html> <head> <meta name="viewport" content="width=device-width" /> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> <title>${mail_subject}</title> <style> /* ------------------------------------- GLOBAL RESETS ------------------------------------- */ /*All the styling goes here*/ img { border: none; -ms-interpolation-mode: bicubic; max-width: 100%; } body { background-color: #f6f6f6; font-family: Helvetica, Arial, serif; -webkit-font-smoothing: antialiased; font-size: 14px; line-height: 1.4; margin: 0; padding: 0; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; } table { border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; } table td { font-family: Helvetica, Arial, serif; font-size: 14px; vertical-align: top; } /* ------------------------------------- BODY & CONTAINER ------------------------------------- */ .body { background-color: #f6f6f6; width: 100%; } /* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */ .container { display: block; margin: 0 auto !important; /* makes it centered */ max-width: 580px; padding: 10px; width: 580px; } @media screen and (max-width: 580px) { .container { width: 100% !important; max-width: 100% !important; min-width: 320px !important; } .content { max-width: 100% !important; } } /* ------------------------------------- HEADER, FOOTER, MAIN ------------------------------------- */ .wrapper { box-sizing: border-box; padding: 20px; } .footer { clear: both; margin-top: 10px; text-align: center; width: 100%; } .footer td, .footer p, .footer span, .footer a { color: #999999; font-size: 12px; text-align: center; } /* ------------------------------------- TYPOGRAPHY ------------------------------------- */ h1, h2, h3, h4 { color: #000000; font-family: Helvetica, Arial, serif; font-weight: 400; line-height: 1.4; margin: 0; margin-bottom: 10px; } h1 { font-size: 35px; font-weight: 300; text-align: center; text-transform: capitalize; } p, ul, ol, pre { font-family: Helvetica, Arial, serif; font-size: 14px; font-weight: normal; margin: 0; margin-bottom: 15px; } p li, ul li, ol li { list-style-position: inside; margin-left: 5px; } a { color: #1687e8; text-decoration: underline; } /* ------------------------------------- BUTTONS ------------------------------------- */ .btn { box-sizing: border-box; width: 100%; } .btn>tbody>tr>td { padding-bottom: 15px; } .btn table { width: auto; } .btn table td { background-color: #ffffff; border-radius: 5px; text-align: center; } .btn a { background-color: #ffffff; border: solid 1px #1687e8; border-radius: 5px; box-sizing: border-box; color: #1687e8; cursor: pointer; display: inline-block; font-size: 14px; font-weight: bold; margin: 0; padding: 12px 25px; text-decoration: none; text-transform: capitalize; } .btn-primary table td { background-color: #1687e8; } .btn-primary a { background-color: #1687e8; border-color: #1687e8; color: #ffffff; } /* ------------------------------------- OTHER STYLES THAT MIGHT BE USEFUL ------------------------------------- */ .last { margin-bottom: 0; } .first { margin-top: 0; } .align-center { text-align: center; } .align-right { text-align: right; } .align-left { text-align: left; } .clear { clear: both; } .mt0 { margin-top: 0; } .mb0 { margin-bottom: 0; } .preheader { color: transparent; display: none; height: 0; max-height: 0; max-width: 0; opacity: 0; overflow: hidden; mso-hide: all; visibility: hidden; width: 0; } .powered-by a { text-decoration: none; } hr { border: 0; border-bottom: 1px solid #f6f6f6; margin: 20px 0; } /* ------------------------------------- RESPONSIVE AND MOBILE FRIENDLY STYLES ------------------------------------- */ @media only screen and (max-width: 620px) { table[class=body] h1 { font-size: 28px !important; margin-bottom: 10px !important; } table[class=body] p, table[class=body] ul, table[class=body] ol, table[class=body] td, table[class=body] span, table[class=body] a { font-size: 16px !important; } table[class=body] .wrapper, table[class=body] .article { padding: 10px !important; } table[class=body] .content { padding: 0 !important; } table[class=body] .container { padding: 0 !important; width: 100% !important; } table[class=body] .main { border-left-width: 0 !important; border-radius: 0 !important; border-right-width: 0 !important; margin-top: 5px; } table[class=body] .btn table { width: 100% !important; } table[class=body] .btn a { width: 100% !important; } table[class=body] .img-responsive { height: auto !important; max-width: 100% !important; width: auto !important; } small { font-size: 10px; } } /* ------------------------------------- PRESERVE THESE STYLES IN THE HEAD ------------------------------------- */ @media all { .ExternalClass { width: 100%; } .ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div { line-height: 100%; } .apple-link a { color: inherit !important; font-family: inherit !important; font-size: inherit !important; font-weight: inherit !important; line-height: inherit !important; text-decoration: none !important; } .btn-primary table td:hover { background-color: #1687e8 !important; } .btn-primary a:hover { background-color: #1687e8 !important; border-color: #1687e8 !important; } } </style> </head> <body class=""> <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="background-color: #f6f6f6; width: 100%;"> <tr> <td>&nbsp;</td> <td class="container" style="display: block; margin: 0 auto !important; max-width: 580px; padding: 0px; width: 580px; "> <div class="content" style="box-sizing: border-box; display: block; margin: 0 auto; max-width: 580px; padding: 15px;"> <!-- START CENTERED WHITE CONTAINER --> <table role="presentation" class="main" style="background: #ffffff; border-radius: 3px; width: 100%; "> <!-- START MAIN CONTENT AREA --> <tr> <td class="wrapper" > <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="width: 100%; color: white !important;"> <tr> <td style="padding: 20px;"> <div style="text-align: center;"> <img width="200" src="cid:companyLogo" /> </div> <hr id="title-hr" style="background-color:#1687e8; height:2px; margin: 20px 0; margin-top:0px;"> <p style="text-align: center"> <h2 style="display: block; text-align: center; font-weight: bold;"> ${mail_subject} </h2> <h4 style="display: block;"> <span style="display: block; text-align: center;"> La password del tuo account <b>${mail_user}</b> è stata modificata di recente. </span> </h4> <h4 style="display: block; text-align: center;"> <span style="display: block; margin-left: 15px;"> Se non riconosci questa attività contatta: </span> ${support_mail} </h4> <br> <h4 style="text-align: center;"> <span style="display: block; text-align: center; font-size: smaller;"> Il Team di Time Sheet </span> </p> </td> </tr> </table> </td> </tr> <!-- END MAIN CONTENT AREA --> </table> <!-- START FOOTER --> <div class="footer" style="text-align: center;"> <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="width: 100%; text-align: center;"> <tr> <td style="padding-bottom: 10px; padding-top: 10px; color: #999999;"> <small style="color: #999999; font-size: 12px; text-align: center; display: block; margin-bottom: 5px;"> Mail inviata in automatico, si prega di non rispondere.<br> </small> <small style="color: #999999; font-size: 12px; text-align: center; "> Copyright © 2018 Time Sheet. </small> </td> </tr> <tr> </tr> </table> </div> <!-- END FOOTER --> <!-- END CENTERED WHITE CONTAINER --> </div> </td> <td>&nbsp;</td> </tr> </table> </body> </html> '),
('RESET_PSW_EMAIL', 'IT', 'Istruzioni Reset Password', '<!doctype html> <html> <head> <meta name="viewport" content="width=device-width" /> <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> <title>${mail_subject}</title> <style> /* ------------------------------------- GLOBAL RESETS ------------------------------------- */ /*All the styling goes here*/ img { border: none; -ms-interpolation-mode: bicubic; max-width: 100%; } body { background-color: #f6f6f6; font-family: Helvetica, Arial, serif; -webkit-font-smoothing: antialiased; font-size: 14px; line-height: 1.4; margin: 0; padding: 0; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; } table { border-collapse: separate; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; } table td { font-family: Helvetica, Arial, serif; font-size: 14px; vertical-align: top; } /* ------------------------------------- BODY & CONTAINER ------------------------------------- */ .body { background-color: #f6f6f6; width: 100%; } /* Set a max-width, and make it display as block so it will automatically stretch to that width, but will also shrink down on a phone or something */ .container { display: block; margin: 0 auto !important; /* makes it centered */ max-width: 580px; padding: 10px; width: 580px; } @media screen and (max-width: 580px) { .container { width: 100% !important; max-width: 100% !important; min-width: 320px !important; } .content { max-width: 100% !important; } } /* ------------------------------------- HEADER, FOOTER, MAIN ------------------------------------- */ .wrapper { box-sizing: border-box; padding: 20px; } .footer { clear: both; margin-top: 10px; text-align: center; width: 100%; } .footer td, .footer p, .footer span, .footer a { color: #999999; font-size: 12px; text-align: center; } /* ------------------------------------- TYPOGRAPHY ------------------------------------- */ h1, h2, h3, h4 { color: #000000; font-family: Helvetica, Arial, serif; font-weight: 400; line-height: 1.4; margin: 0; margin-bottom: 10px; } h1 { font-size: 35px; font-weight: 300; text-align: center; text-transform: capitalize; } p, ul, ol, pre { font-family: Helvetica, Arial, serif; font-size: 14px; font-weight: normal; margin: 0; margin-bottom: 15px; } p li, ul li, ol li { list-style-position: inside; margin-left: 5px; } a { color: #1687e8; text-decoration: underline; } /* ------------------------------------- BUTTONS ------------------------------------- */ .btn { box-sizing: border-box; width: 100%; } .btn>tbody>tr>td { padding-bottom: 15px; } .btn table { width: auto; } .btn table td { background-color: #ffffff; border-radius: 5px; text-align: center; } .btn a { background-color: #ffffff; border: solid 1px #1687e8; border-radius: 5px; box-sizing: border-box; color: #1687e8; cursor: pointer; display: inline-block; font-size: 14px; font-weight: bold; margin: 0; padding: 12px 25px; text-decoration: none; text-transform: capitalize; } .btn-primary table td { background-color: #1687e8; } .btn-primary a { background-color: #1687e8; border-color: #1687e8; color: #ffffff; } /* ------------------------------------- OTHER STYLES THAT MIGHT BE USEFUL ------------------------------------- */ .last { margin-bottom: 0; } .first { margin-top: 0; } .align-center { text-align: center; } .align-right { text-align: right; } .align-left { text-align: left; } .clear { clear: both; } .mt0 { margin-top: 0; } .mb0 { margin-bottom: 0; } .preheader { color: transparent; display: none; height: 0; max-height: 0; max-width: 0; opacity: 0; overflow: hidden; mso-hide: all; visibility: hidden; width: 0; } .powered-by a { text-decoration: none; } hr { border: 0; border-bottom: 1px solid #f6f6f6; margin: 20px 0; } /* ------------------------------------- RESPONSIVE AND MOBILE FRIENDLY STYLES ------------------------------------- */ @media only screen and (max-width: 620px) { table[class=body] h1 { font-size: 28px !important; margin-bottom: 10px !important; } table[class=body] p, table[class=body] ul, table[class=body] ol, table[class=body] td, table[class=body] span, table[class=body] a { font-size: 16px !important; } table[class=body] .wrapper, table[class=body] .article { padding: 10px !important; } table[class=body] .content { padding: 0 !important; } table[class=body] .container { padding: 0 !important; width: 100% !important; } table[class=body] .main { border-left-width: 0 !important; border-radius: 0 !important; border-right-width: 0 !important; margin-top: 5px; } table[class=body] .btn table { width: 100% !important; } table[class=body] .btn a { width: 100% !important; } table[class=body] .img-responsive { height: auto !important; max-width: 100% !important; width: auto !important; } small { font-size: 10px; } } /* ------------------------------------- PRESERVE THESE STYLES IN THE HEAD ------------------------------------- */ @media all { .ExternalClass { width: 100%; } .ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div { line-height: 100%; } .apple-link a { color: inherit !important; font-family: inherit !important; font-size: inherit !important; font-weight: inherit !important; line-height: inherit !important; text-decoration: none !important; } .btn-primary table td:hover { background-color: #1687e8 !important; } .btn-primary a:hover { background-color: #1687e8 !important; border-color: #1687e8 !important; } } </style> </head> <body class=""> <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="background-color: #f6f6f6; width: 100%;"> <tr> <td>&nbsp;</td> <td class="container" style="display: block; margin: 0 auto !important; max-width: 580px; padding: 0px; width: 580px; "> <div class="content" style="box-sizing: border-box; display: block; margin: 0 auto; max-width: 580px; padding: 15px;"> <!-- START CENTERED WHITE CONTAINER --> <table role="presentation" class="main" style="background: #ffffff; border-radius: 3px; width: 100%; "> <!-- START MAIN CONTENT AREA --> <tr> <td class="wrapper" > <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="width: 100%; color: white !important;"> <tr> <td style="padding: 20px;"> <div style="text-align: center;"> <img width="200" src="cid:companyLogo" /> </div> <hr id="title-hr" style="background-color:#1687e8; height:2px; margin: 20px 0; margin-top:0px;"> <p style="text-align: center"> <h2 style="display: block; text-align: center; font-weight: bold;"> ${mail_subject} </h2> <h4 style="display: block;"> <span style="display: block; text-align: center;"> Per completare il reset della password del tuo account fai click sul seguente bottone: </span> </h4> <div style="width: 100%; text-align: center; margin-top: 15px; margin-bottom: 15px;"> <table role="presentation" cellpadding="0" cellspacing="0" style="width: 300px; max-width: 100%; text-align: center; margin: 0 auto;"> <tr> <td style="border-radius: 2px; text-align: center; height: 2em; background-color: #3899ec;"> <span style=" padding: 0.8em; background-color: #3899ec; display: block; text-align: center;"> <a style="background-color: #3899ec; text-decoration: none; color: white; padding: 0.8em;" href="${reset_psw_link}"> Reset Password </a> </span> </td> </tr> </table> </div> <h4 style="display: block; text-align: center;"> <small style="display: block; text-align: center;"> Se il link precedente non dovesse funzionare copia e incolla sul tuo browser il seguente link: ${reset_psw_link} </small> </h4> <h4 style="display: block; text-align: center;"> <span style="display: block; margin-left: 15px;text-align: center; width: 100%;"> Se non hai richiesto il reset della password ti preghiamo di ignorare questa email </span> </h4> <br> <h4 style="text-align: center;"> <span style="display: block; text-align: center; font-size: smaller;"> Il Team di Time Sheet </span> </p> </td> </tr> </table> </td> </tr> <!-- END MAIN CONTENT AREA --> </table> <!-- START FOOTER --> <div class="footer" style="text-align: center;"> <table role="presentation" border="0" cellpadding="0" cellspacing="0" style="width: 100%; text-align: center;"> <tr> <td style="padding-bottom: 10px; padding-top: 10px; color: #999999;"> <small style="color: #999999; font-size: 12px; text-align: center; display: block; margin-bottom: 5px;"> Mail inviata in automatico, si prega di non rispondere.<br> </small> <small style="color: #999999; font-size: 12px; text-align: center; "> Copyright © 2018 Time Sheet. </small> </td> </tr> <tr> </tr> </table> </div> <!-- END FOOTER --> <!-- END CENTERED WHITE CONTAINER --> </div> </td> <td>&nbsp;</td> </tr> </table> </body> </html> ');


INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES  ('PROFILE_TWILIO', 'ACCOUNT_SID', 'xxxxx', TRUE, TRUE),  
		('PROFILE_TWILIO', 'AUTH_TOKEN', 'xxxx', TRUE, TRUE),
		('PROFILE_TWILIO', 'TWILIO_NUMBER', '+33333', TRUE, TRUE);   



INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES  ('FRONTEND_LINK', 'WEB_APP_RESET_PSW_LINK', 'http://localhost:3000/#/password_reset', TRUE, TRUE);      
		
INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES  ('FOOD_VOURCHER', 'MIN_WORKED_HOURS_TO_REQUEST_IT', '4', TRUE, TRUE),
		('FOOD_VOURCHER', 'ENABLED', 'TRUE', TRUE, TRUE);
	
INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES  ('SUPPORT', 'SECURITY_SUPPORT_MAIL', 'admin@gmail.com', TRUE, TRUE);
		
		
INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES  ('PROFILE_SMTP', 'MAIL_SMTP_HOST', 'smtp.gmail.com', TRUE, TRUE),  
		('PROFILE_SMTP', 'MAIL_SMTP_USER', 'admin@gmail.com', TRUE, TRUE),
		('PROFILE_SMTP', 'MAIL_SMTP_PASSWORD', 'xxxxx', TRUE, TRUE),
		('PROFILE_SMTP', 'MAIL_SMTP_PORT', '587', TRUE, TRUE),
		('PROFILE_SMTP', 'MAIL_SMTP_AUTH', 'true', TRUE, TRUE),
		('PROFILE_SMTP', 'MAIL_SMTP_STARTTLS_ENABLE', 'true', TRUE, TRUE),
		('PROFILE_SMTP', 'MAIL_SMTP_SSL_ENABLE', 'false', TRUE, TRUE),
		('PROFILE_SMTP', 'MAIL_TRANSPORT_PROTOCOL', 'smtps', TRUE, TRUE);
		
INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES  ('FILE_MANAGER_CONFIG', 'DEFAULT_FILE_MANAGER', 'StandardFSFileManager', TRUE, TRUE),
		('FILE_MANAGER_CONFIG', 'DEFAULT_FILE_MANAGER_CRYPTED', 'EncryptedFSFileManager', TRUE, TRUE),
		('FILE_MANAGER_CONFIG', 'DEFAULT_IMAGE_CONVERSION_EXTENSION', 'jpg', TRUE, TRUE),
		('FILE_MANAGER_CONFIG', 'DEFAULT_COMPRESSION_LEVEL', '0.40', TRUE, TRUE);


INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES  ('REGISTRATION_SETTING_CONFIG', 'UNIQUE_CONTACTS', 'TRUE', TRUE, TRUE);

INSERT INTO USER_CONTACT_TYPE(C_TYPE) VALUES ('PHONE_NUMBER'), 
('EMAIL_ADDRESS'), ('PHONE_NUMBER_REPLACEMENT');


INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'RUOLO_ABILITATO_INCARICO_EDIT', 'PROJECT_MANAGER', '-', TRUE, TRUE),
( 'RUOLO_ABILITATO_INCARICO_EDIT', 'DELIVERY_MANAGER', '-', TRUE, TRUE),
( 'RUOLO_ABILITATO_INCARICO_EDIT', 'ACCOUNT_MANAGER', '-', TRUE, TRUE);


INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'REPORT', 'REPORT_CREATOR', 'StandardXLSReportCreator', TRUE, TRUE);




INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_PER_CALCOLO_MENSILE', 'FERIE', 'IT_FERIE_1', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_PER_CALCOLO_MENSILE', 'ROL', 'IT_ROL_2', TRUE, TRUE);



INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'PUBLIC_HOLIDAYS', 'CAPODANNO', '01-01-2020', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'PUBLIC_HOLIDAYS', 'EPIFANIA', '06-01-2020', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'PUBLIC_HOLIDAYS', 'PASQUETTA', '13-04-2020', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'PUBLIC_HOLIDAYS', 'FESTA_DELLA_LIBERAZIONE', '24-04-2020', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'PUBLIC_HOLIDAYS', 'FESTA_DEI_LAVORATORI', '01-05-2020', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'PUBLIC_HOLIDAYS', 'FERRAGOSTO', '15-08-2020', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'PUBLIC_HOLIDAYS', 'IMMACOLATA', '08-12-2020', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'PUBLIC_HOLIDAYS', 'NATALE', '25-12-2020', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'PUBLIC_HOLIDAYS', 'SANTO_STEFANO', '26-12-2020', TRUE, TRUE);


INSERT INTO user_level(level, monthly_vacation_days, monthly_leave_hours, bank_hour_enabled, extra_work_paid) VALUES 
('JUNIOR', 1.87, 8, true, true), 
('MID', 1.87, 8, true, true), 
('SENIOR', 1.87, 8, true, true), 
('MANAGER', 1.87, 8, false, false), 
('SENIOR_MANAGER', 1.87, 8, false, false), 
('PARTNER', 1.87, 8, false, false);


INSERT INTO WORK_TASK(
		CREATED_DATE, LAST_MODIFIED_DATE, LAST_MODIFIED_BY, CREATED_BY, 
		task_code, task_description, billable, activation_date, is_enabled_for_all_user, is_absence_task
)
values (CURRENT_TIMESTAMP at time zone 'utc', CURRENT_TIMESTAMP at time zone 'utc', 
'database', 'database', 'IT_FERIE_1', 'Ferie', FALSE, CURRENT_TIMESTAMP at time zone 'utc', TRUE, TRUE),

(CURRENT_TIMESTAMP at time zone 'utc', CURRENT_TIMESTAMP at time zone 'utc', 
'database', 'database', 'IT_ROL_2', 'ROL/Festività soppresse. Ore di permesso', FALSE, CURRENT_TIMESTAMP at time zone 'utc', TRUE, TRUE),

(CURRENT_TIMESTAMP at time zone 'utc', CURRENT_TIMESTAMP at time zone 'utc', 
'database', 'database', 'IT_MALATTIA_3', 'Malattia', FALSE, CURRENT_TIMESTAMP at time zone 'utc', TRUE, TRUE),

(CURRENT_TIMESTAMP at time zone 'utc', CURRENT_TIMESTAMP at time zone 'utc', 
'database', 'database', 'IT_P_MATRIMONIALE_4', 'Permesso matrimoniale', FALSE, CURRENT_TIMESTAMP at time zone 'utc', TRUE, TRUE),

(CURRENT_TIMESTAMP at time zone 'utc', CURRENT_TIMESTAMP at time zone 'utc', 
'database', 'database', 'IT_CONGEDO_PATERNITA_5', 'Congedo paternità', FALSE, CURRENT_TIMESTAMP at time zone 'utc', TRUE, TRUE),

(CURRENT_TIMESTAMP at time zone 'utc', CURRENT_TIMESTAMP at time zone 'utc', 
'database', 'database', 'IT_PERMESSO_STUDIO_6', 'Permesso ferie studio', FALSE, CURRENT_TIMESTAMP at time zone 'utc', TRUE, TRUE),

(CURRENT_TIMESTAMP at time zone 'utc', CURRENT_TIMESTAMP at time zone 'utc', 
'database', 'database', 'IT_P_LUTTO_FAMILIARE_7', 'Permesso lutto familiare', FALSE, CURRENT_TIMESTAMP at time zone 'utc', TRUE, TRUE),

(CURRENT_TIMESTAMP at time zone 'utc', CURRENT_TIMESTAMP at time zone 'utc', 
'database', 'database', 'IT_P_DONAZIONE_SANGUE_8', 'Permesso donazione sangue', FALSE, CURRENT_TIMESTAMP at time zone 'utc', TRUE, TRUE),

(CURRENT_TIMESTAMP at time zone 'utc', CURRENT_TIMESTAMP at time zone 'utc', 
'database', 'database', 'IT_FAMILY_DAY_9', 'Family Day', FALSE, CURRENT_TIMESTAMP at time zone 'utc', TRUE, TRUE);



INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI', 'FERIE', 'IT_FERIE_1', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI', 'ROL', 'IT_ROL_2', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI', 'MALATTIA', 'IT_MALATTIA_3', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI', 'PERMESSO_MATRIMONIALE', 'IT_P_MATRIMONIALE_4', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI', 'CONGEDO_PATERNITA', 'IT_CONGEDO_PATERNITA_5', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI', 'PERMESSO_STUDIO', 'IT_PERMESSO_STUDIO_6', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI', 'PERMESSO_LUTTO_FAMILIARE', 'IT_P_LUTTO_FAMILIARE_7', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI', 'PERMESSO_DONAZIONE_SANGUE', 'IT_P_DONAZIONE_SANGUE_8', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI', 'FAMILY_DAY', 'IT_FAMILY_DAY_9', TRUE, TRUE);



INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI_COLOR', 'FERIE', '#034196', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI_COLOR', 'ROL', '#dc3545', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI_COLOR', 'MALATTIA', '#802e2c', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI_COLOR', 'PERMESSO_MATRIMONIALE', '#fb9696', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI_COLOR', 'CONGEDO_PATERNITA', '#6c757d', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI_COLOR', 'PERMESSO_STUDIO', '#f0ad4e', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI_COLOR', 'PERMESSO_LUTTO_FAMILIARE', '#000000', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI_COLOR', 'PERMESSO_DONAZIONE_SANGUE', '#d85a27', TRUE, TRUE);

INSERT INTO GLOBAL_CONFIGURATIONS(SETTING_AREA, SETTING_KEY, SETTING_VALUE, VISIBLE, EDITABLE)
VALUES( 'CODICI_INCARICO_SPECIALI_COLOR', 'FAMILY_DAY', '#43a7e8', TRUE, TRUE);


insert into personal_document_type(type, extensions_supported) values 
('PERSONAL_PHOTO', '.jpg'),
('CLEAN_POLICE_CERTIFICATION', '.pdf'),
('IDENTITY_CARD', '.pdf'),
('ADDRESS_SELF_CERTIFICATION', '.pdf'),
('FISCAL_CODE', '.pdf'),
('GDPR', '.pdf'),
('ETHICAL_CODE', '.pdf'),
('TFR_DEST', '.pdf'),
('EMPLOYMENT_CONTRACT', '.pdf');


INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('LOGGED_USER_PERMISSION');


INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TASK_DETAILS_READ');

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('INCARICO_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('INCARICO_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('INCARICO_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('INCARICO_DELETE');

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('SEDE_LAVORATIVA_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('SEDE_LAVORATIVA_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('SEDE_LAVORATIVA_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('SEDE_LAVORATIVA_DELETE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TEAM_INCARICO_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TEAM_INCARICO_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TEAM_INCARICO_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TEAM_INCARICO_DELETE');

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('PAYCHECK_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('PAYCHECK_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('PAYCHECK_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('PAYCHECK_DELETE');

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('PERSONAL_DOCUMENT_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('PERSONAL_DOCUMENT_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('PERSONAL_DOCUMENT_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('PERSONAL_DOCUMENT_DELETE');

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_PROFILE_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_PROFILE_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_PROFILE_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_PROFILE_DELETE');

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_LEVEL_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_LEVEL_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_LEVEL_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_LEVEL_DELETE');

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TURNSTILE_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TURNSTILE_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TURNSTILE_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TURNSTILE_DELETE');	

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_ATTENDANCE_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_ATTENDANCE_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_ATTENDANCE_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('USER_ATTENDANCE_DELETE');	

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('REPORT_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('REPORT_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('REPORT_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('REPORT_DELETE');	

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('COMPANY_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('COMPANY_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('COMPANY_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('COMPANY_DELETE');	

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TASK_COMPLETION_LOCK_READ');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TASK_COMPLETION_LOCK_UPDATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TASK_COMPLETION_LOCK_CREATE');
INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('TASK_COMPLETION_LOCK_DELETE');	

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('MANAGE_EXPENSE_REPORT');	

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('MANAGE_GLOBAL_CONFIGURATIONS');

INSERT INTO PERMISSIONS(AUTHORITY) VALUES ('ADMINISTRATION_PERMISSION');


INSERT INTO permission_group_label(NAME) values ('LOGGED_USER');
INSERT INTO permission_group_label(NAME) values ('MANAGER');
INSERT INTO permission_group_label(NAME) values ('HR_BUSINESS_PARTNER');
INSERT INTO permission_group_label(NAME) values ('ADMINISTRATION');



/*popoliamo tutti i possibili permessi dell applicativo*/
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('LOGGED_USER', 'LOGGED_USER_PERMISSION');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('LOGGED_USER', 'INCARICO_READ');



INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('MANAGER', 'LOGGED_USER_PERMISSION');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('MANAGER', 'INCARICO_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('MANAGER', 'INCARICO_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('MANAGER', 'INCARICO_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('MANAGER', 'INCARICO_DELETE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('MANAGER', 'SEDE_LAVORATIVA_READ');



INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES('HR_BUSINESS_PARTNER', 'LOGGED_USER_PERMISSION');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'INCARICO_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'INCARICO_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'INCARICO_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'INCARICO_DELETE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'SEDE_LAVORATIVA_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'SEDE_LAVORATIVA_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'SEDE_LAVORATIVA_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'SEDE_LAVORATIVA_DELETE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'TEAM_INCARICO_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'TEAM_INCARICO_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'TEAM_INCARICO_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'TEAM_INCARICO_DELETE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'PAYCHECK_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'PAYCHECK_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'PAYCHECK_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'PAYCHECK_DELETE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'PERSONAL_DOCUMENT_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'PERSONAL_DOCUMENT_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'PERSONAL_DOCUMENT_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'PERSONAL_DOCUMENT_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_PROFILE_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_PROFILE_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_PROFILE_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_PROFILE_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'TASK_DETAILS_READ');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_LEVEL_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_LEVEL_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_LEVEL_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_LEVEL_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'REPORT_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'REPORT_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'REPORT_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'REPORT_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_ATTENDANCE_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_ATTENDANCE_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_ATTENDANCE_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'USER_ATTENDANCE_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('HR_BUSINESS_PARTNER', 'MANAGE_EXPENSE_REPORT');



INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'LOGGED_USER_PERMISSION');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'INCARICO_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'INCARICO_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'INCARICO_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'INCARICO_DELETE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'SEDE_LAVORATIVA_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'SEDE_LAVORATIVA_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'SEDE_LAVORATIVA_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'SEDE_LAVORATIVA_DELETE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TEAM_INCARICO_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TEAM_INCARICO_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TEAM_INCARICO_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TEAM_INCARICO_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'PAYCHECK_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'PAYCHECK_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'PAYCHECK_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'PAYCHECK_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'COMPANY_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'COMPANY_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'COMPANY_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'COMPANY_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'PERSONAL_DOCUMENT_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'PERSONAL_DOCUMENT_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'PERSONAL_DOCUMENT_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'PERSONAL_DOCUMENT_DELETE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_PROFILE_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_PROFILE_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_PROFILE_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_PROFILE_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TASK_DETAILS_READ');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_LEVEL_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_LEVEL_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_LEVEL_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_LEVEL_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TURNSTILE_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TURNSTILE_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TURNSTILE_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TURNSTILE_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_ATTENDANCE_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_ATTENDANCE_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_ATTENDANCE_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'USER_ATTENDANCE_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'REPORT_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'REPORT_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'REPORT_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'REPORT_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TASK_COMPLETION_LOCK_READ');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TASK_COMPLETION_LOCK_UPDATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TASK_COMPLETION_LOCK_CREATE');
INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'TASK_COMPLETION_LOCK_DELETE');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'MANAGE_EXPENSE_REPORT');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'MANAGE_GLOBAL_CONFIGURATIONS');

INSERT INTO PERMISSION_GROUP(NAME, FK_AUTHORITY) VALUES ('ADMINISTRATION', 'ADMINISTRATION_PERMISSION');






--------------------------------CHANGE USERNAME----------------------
	
INSERT INTO USERS_AUTH_DETAILS( USERNAME, HASHED_PASSWORD, LAST_PASSWORD_CHANGE_DATE, ISENABLED, 
				ISACCOUNTNONEXPIRED, ISACCOUNTNONLOCKED, ISCREDENTIALSNONEXPIRED, two_fa_enabled, 
				permission_group_name, must_reset_password)
VALUES ('admin@xxxxxx.it','$2a$10$0.ZOiHKGV4PPu1a6NERS3.lSw1Prko7hQEGFOrwvvfkqbYottMa/i', 
CURRENT_TIMESTAMP at time zone 'utc', true, true, true, true, false, 'ADMINISTRATION', false);	



/*inseriamo i permessi dell'utente superuser*/
INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'LOGGED_USER_PERMISSION' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';

INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'INCARICO_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'INCARICO_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'INCARICO_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'INCARICO_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TEAM_INCARICO_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';

INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TEAM_INCARICO_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';

INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TEAM_INCARICO_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TEAM_INCARICO_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'SEDE_LAVORATIVA_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'SEDE_LAVORATIVA_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'SEDE_LAVORATIVA_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'SEDE_LAVORATIVA_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	

INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'PERSONAL_DOCUMENT_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'PERSONAL_DOCUMENT_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'PERSONAL_DOCUMENT_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'PERSONAL_DOCUMENT_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'PAYCHECK_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'PAYCHECK_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'PAYCHECK_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'PAYCHECK_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	

	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_PROFILE_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_PROFILE_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_PROFILE_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_PROFILE_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'COMPANY_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'COMPANY_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'COMPANY_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'COMPANY_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';


	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TASK_DETAILS_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	
		INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_LEVEL_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_LEVEL_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_LEVEL_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_LEVEL_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	
		INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TURNSTILE_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TURNSTILE_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TURNSTILE_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TURNSTILE_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	
		INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_ATTENDANCE_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_ATTENDANCE_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_ATTENDANCE_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'USER_ATTENDANCE_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	
INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'REPORT_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'REPORT_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'REPORT_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'REPORT_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	

	
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TASK_COMPLETION_LOCK_READ' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TASK_COMPLETION_LOCK_UPDATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TASK_COMPLETION_LOCK_CREATE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY)  
select id as FK_ID_USERS_AUTH_DETAILS, 'TASK_COMPLETION_LOCK_DELETE' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
	
INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY) 
select id as FK_ID_USERS_AUTH_DETAILS, 'MANAGE_GLOBAL_CONFIGURATIONS' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY) 
select id as FK_ID_USERS_AUTH_DETAILS, 'ADMINISTRATION_PERMISSION' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it';
	
INSERT INTO AUTHORITIES( FK_ID_USERS_AUTH_DETAILS, AUTHORITY) 
select id as FK_ID_USERS_AUTH_DETAILS, 'MANAGE_EXPENSE_REPORT' from USERS_AUTH_DETAILS where 
	USERNAME='admin@xxxxxx.it'; 





INSERT INTO COMPANY(NAME, DESCRIPTION, IS_ROOT) VALUES ('COMPANY NAME', 'COMPANY DESCRIPTION', TRUE);

INSERT INTO user_profile(id, name, surname, sex, email, phone_number, birth_date)
select uad.id as FK_ID_USERS_AUTH_DETAILS , 'Admin', 'Admin', 'M', 'admin@xxxxxx.it', '+39 333333333', '2000-09-07'
from USERS_AUTH_DETAILS uad  where uad.username='admin@xxxxxx.it';
	
	
INSERT INTO user_contacts(
	fk_id_users_auth_details, user_contact_type, c_value, verified)
	select uad.id as fk_id_users_auth_details, 'PHONE_NUMBER', '+39 333333333', true 
	from USERS_AUTH_DETAILS uad  where uad.username='admin@xxxxxx.it';
	
INSERT INTO user_contacts(
	fk_id_users_auth_details, user_contact_type, c_value, verified)
	select uad.id as fk_id_users_auth_details, 'EMAIL_ADDRESS', 'admin@xxxxxx.it', true  
	from USERS_AUTH_DETAILS uad  where uad.username='admin@xxxxxx.it';



INSERT INTO  database_version (ID, VERSION) VALUES (1, '1.0.0');
-- VERSION 1.0.0 QUERIES end


	
	
	
 
 
 
 
 
        
