Name: Seena Rowhani
Student #: 100945353
## Files

All runnable files are prefixed with `run` within `src/main/java/`

Make sure to run the server / intermediate first, client won't attempt to resend content.

When running in eclipse you can switch run windows by clicking the dropdown from console view.

These will include
 - RunClientInstance
    - Instantiates a client that runs test scenarios (11 send/rec attempts)
 - RunServerInstance
    - Instantiates server
 - RunIntermediateHostInstance
    - Runs middle man server

Configuration file is located in `config/Properties`
    - note: my set up didn't allow me to serve from ports under 1000 w/o sudo
    - if the same applies for you under your environment, please do adjust ports in config


The UML / UCM is included as png files in root dir.