RIFL Extractor
==============

**RIFL Extractor** is an *Annotation Processor* for extracting information flow specification from the annotation in the source
 into the RIFL XML format. 
 
    Version: 0.1-prototype
    License: GPL-v3
    Author: Alexander Weigl <weigl@kit.edu>
 
 
Getting Started
===============

* Download this project

```    
    $ git clone https://github.com/wadoon/rifleextractor.git
```
   
* Run ANT:

```    
    $ ant artifact.riflap
```         
* Test with the FristExample.java

```    
    $ cd example/src/
    $ javac -cp ../../out/artifacts/riflap/riflap.jar edu/kit/iti/rifl/FirstTest.java
```    

TODO 
====

* [ ] Produced RIFL xml is not quite right
* [ ] Category support
* [ ] Arbitrary Lattice
* [ ] Information Flow Policies as Annotations
* [ ] Generation of multiple RIFL specs for different classes
