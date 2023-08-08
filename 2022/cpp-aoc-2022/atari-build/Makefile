PREFIX=~/opt/cross-mint-12.2.0-thorsten-20230112/bin/m68k-atari-mint-

CXXFLAGS=-O3 -m68000
CC=$(PREFIX)gcc
CXX=$(PREFIX)g++
AS=$(PREFIX)as

EXECUTABLE=aoc2023.tos

all: $(EXECUTABLE) $(TEST_EXECUTABLE)

$(EXECUTABLE): main.o day01.o hello.o
	${CXX} ${CXXFLAGS} main.o day01.o hello.o -o $(EXECUTABLE) -lgcc

%.o : ../src/%.cpp ../src/*.hpp
	${CXX} -DWAIT_FOR_KEYPRESS_BEFORE_QUIT -c $(CXXFLAGS) $< -o $@

clean:
	rm -f *.o $(EXECUTABLE)
