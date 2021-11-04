# CS-440-Group-Project

Changes I Made

1) Updated the "AndroidManifest.xml" file to add the "RoleActivity" Java Class
2) Created a new Java Class called "RoleActivity.java"
3) In the "RoleActivity.java" I have 
4) (a) onCreateMethod that displays the role_screen.xml
5) (b) "handleExplorerBtn" a handler for when the user clicks on the "Explorer" button
6) (b1) The user must enter the correct login (admin:admin) and than click on "Explorer" in order to navigate to the map screen
7) (c) "handleAssassinBtn" a handler for when the user clicks on the "Assassin" button
8) (c1) The user must enter the correct login (admin:admin) and than click on "Assassin" in order to navigate to the map screen
9) (d) "getPermission" method
10) (e) "onRequestPermissionsResult" method
11) Created a new xml layout file called "role_screen.xml" 
12) The methods "getPermission()" and "onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)" were moved
from "MainActivity.java" to "RoleActivity.java"
5) I added a new private method called "getRoleScreen" inside "MainActivity.java"
6) I modified "public void handleStartBtn(View view) { getPermission(); }" to "public void handleStartBtn(View view) { getRoleScreen(); }"
7) I modified the methods "getPermission()" and "onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)", inside of
them I call "RoleActivity.this" instead of "MainActivity.this" because they were moved inside the "RoleActivity.java" class
