
SHELL := /bin/bash
JRATE_HOME := /opt/jRate
GCJ := $(JRATE_HOME)/bin/jRate-gcj
CLASSPATH := $(JRATE_HOME)/share/java/libgcj-3.3.3.jar

OUTDIR := ./jrate-bin
OUTFILE := run-me.exe
SRCDIR := ./src
MAIN_JAVA_CLASS := rtsjcomponents.example2.Example2

SRC_FILES := $(shell find $(SRCDIR) -name *.java)

.PHONY = all clean

all: $(OUTFILE)

$(OUTFILE):
	mkdir $(OUTDIR); \
        $(GCJ) --classpath=$(CLASSPATH) --main=$(MAIN_JAVA_CLASS) -d $(OUTDIR) -o $(OUTDIR)/$(OUTFILE) $(SRC_FILES);

clean:
	rm -R $(OUTDIR);

