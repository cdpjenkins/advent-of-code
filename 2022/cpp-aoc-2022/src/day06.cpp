#include "day06.hpp"

#include <iostream>
#include <map>

static int find_first_marker(std::string& input, int window_size) {
    std::map<char, int> frequencies;

    for (int i = 0; i < input.length(); i++) {
        frequencies[input[i]]++;

        if (i >= window_size) {
            char to_remove = input[i - window_size];
            frequencies[to_remove]--;
            if (frequencies[to_remove] == 0) {
                frequencies.erase(to_remove);
            }
        }

        if (frequencies.size() == window_size) {
            // we must have a single instance of each if this is the first time that the size of frequencies has hit
            // window_size
            return i+1;
        }
    }

    return -1;
}

int day06_part1(std::string input) {
    return find_first_marker(input, 4);
}

int day06_part2(std::string input) {
    return find_first_marker(input, 14);
}
