# Power Launcher
### Custom Launcher for Java and Executables Programs

## License :
This program use the Creative Commons License (https://creativecommons.org/licenses/by-nc-nd/4.0/)

**You are free to:**
- **Share** — copy and redistribute the material in any medium or format

*The licensor cannot revoke these freedoms as long as you follow the license terms.*

**Under the following terms:**
- **Attribution** — You must give appropriate credit, provide a link to the license, and indicate if changes were made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
- **NonCommercial** — You may not use the material for commercial purposes.
- **NoDerivatives** — If you remix, transform, or build upon the material, you may not distribute the modified material. 
- **No additional** restrictions — You may not apply legal terms or technological measures that legally restrict others from doing anything the license permits.

## Configuration :
Power Launcher is easy to use, but there a few conditions :
- You need to use GitHub for your releases
- You need to upload a file to your GitHub Repo
- You need a WebServer or use the default one *(The author is free to stop it and/or make it paid, you can also lose your access to it)*
- The GitHub branch is **ALWAYS** main, can't be changed

### GitHub :
In your main Repo, create a ``infos.txt``file, put this :
```
version=1.0.0
buildNumber=1
```
``version`` is the Current Version of your Program

``buildNumber`` is a number that increase everytime you make a new version of your app, it's important to update it, **Power Launcher's Updater System is based on buildNumber**

When you make a release, **the tag need to be the version of your program, with the example above, the tag will be ``1.0.0``

You can set any description, the only important thing is to have a static App name, like ``app.jar`` or ``app.exe``, **Don't change it, the old launcher version will crash if you do**

### Launcher :
In the same directory of your launcher, edit the ``settings.properties`` file :
```properties
version=1.0.0                                           # Same as GitHub
buildNumber=1                                           # Same as GitHub
file=app.jar                                            # Same as GitHub releases
license=DEMO                                            # Your License key
serverURL=http://charlito33.me/power-launcher/api       # The URL of Default / Your server
launcherName=My Launcher                                # Anything you want
```

``version`` and ``buildNumber`` is for updating, ``file`` is the filename to download and to launch

``license`` is your license, contact the author to get one

*(The author is free to make you pay for it, or disable it at any time)*

``serverURL`` is the license checker serverURL, change it to your custom webserver if you want

``launcherName`` is the name shown when you start the launcher

### Repo :
To make the app working, you need to set the repo URL in the license server

Go to https://charlito33.me/power-launcher/edit-license-repo

Fill your license, your repo *(Like : https://github.com/Charlito33/PowerLauncherTestRepo)* and your secret

Secret is provided by the author when asking for a license

Submit, the repo is now changed on every launcher (**The launcher is online only, for now**)

The launcher use theses infos :
```
https://github.com/Charlito33/PowerLauncherTestRepo
https://raw.githubusercontent.com/Charlito33/PowerLauncherTestRepo/main/infos.txt
```

## Make your Own Webserver :
**Warnings :**
- Making your own webserver is not recommended, if you have a webserver, don't use GitHub...
- When the launcher gets an update, you will need to change your webserver code to update it *(No official code is provided)*

### Few notes :
- License is a key to the Repo
- Launcher make request like that : ``{serverURL}/file?get``

### Why Licenses ?
The license system has been created to avoid abuses, when using the default server it tracks the uses of your license

It's also a good way if you want to "hide" your repo

### License Check :
Create a file named ``get-license``, launcher use these args (GET) :
- ``v`` : License

Return this data (No comment, [v1/v2] = v1 or v2) :
```
status=[true/false] // License is valid or invalid
message=Error message // Not really used when successful
repo=https://github.com/Charlito33/PowerLauncherTestRepo // Only used when status=true
```
