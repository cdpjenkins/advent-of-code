PREFIX=~/opt/cross-mint-13.2.0-thorsten-20230801/bin/m68k-atari-mint-

CXXFLAGS=-O3 -m68000 --std=c++20
CC=$(PREFIX)gcc
CXX=$(PREFIX)g++
AS=$(PREFIX)as

EXECUTABLE=aoc2023.tos

all: $(EXECUTABLE)

%.o : ../src/%.cpp ../src/*.hpp
	${CXX} -DWAIT_FOR_KEYPRESS_BEFORE_QUIT -c $(CXXFLAGS) $< -o $@

$(EXECUTABLE): main.o day01.o day02.o hello.o util.o
	${CXX} ${CXXFLAGS} -o $(EXECUTABLE) $^ -lgcc

clean:
	rm -f *.o $(EXECUTABLE)
