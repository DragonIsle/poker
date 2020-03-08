# Texas holdem poker

This program is command line app that sort texas holdem poker hands, from weakest to strongest one.

Input is a string in the following format: 

<5 board cards> <hand 1> <hand 2> <...> <hand N>

where: 

* <5 board cards> is a 10 character string where each 2 characters encode a card 
* <hand X> is a 4 character string where each 2 characters encode a card, with 2 cards per hand
* <card> is a 2 character string with the first character representing the rank (one of "A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2") and the second character representing the suit (one of "h", "d", "c", "s").

Output will be looks like

<hand 1> <hand 2> <...> <hand n>

where each hand is one of the previously passed in input, but output sorted by power.
If hands are equal in power, they will be presented in output with '=' sign between them

## Executing
To run this app, you need to have installed scala(default version 2.13.1) and sbt (default version 1.3.8). 
You can try to change versions of scala and sbt to oldest ones, 
but make sure that it will not break compilation, building etc.

App can be run by executing 
```
sbt run
```
from project directory


## Tests
There are also test for many different cases of possible table + hands combinations.
To run it, just execute
```
sbt test
```
from project directory
