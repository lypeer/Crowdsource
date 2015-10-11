[1mdiff --git a/app/src/main/java/com/tesmple/crowdsource/activity/RegisterActivity.java b/app/src/main/java/com/tesmple/crowdsource/activity/RegisterActivity.java[m
[1mindex 5423ebc..369d6cc 100644[m
[1m--- a/app/src/main/java/com/tesmple/crowdsource/activity/RegisterActivity.java[m
[1m+++ b/app/src/main/java/com/tesmple/crowdsource/activity/RegisterActivity.java[m
[36m@@ -193,7 +193,7 @@[m [mpublic class RegisterActivity extends AppCompatActivity {[m
                         sendMessage(phoneNum);[m
                     } else {[m
                         Log.e("Register_isExist", e.getMessage() + "===" + e.getCode());[m
[31m-                         App.dismissDialog();[m
[32m+[m[32m                        App.dismissDialog();[m
                         Snackbar.make(btnRegister, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)[m
                                 .setAction("Action", null).show();[m
                     }[m
