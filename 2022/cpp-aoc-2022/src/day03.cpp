#include "day02.hpp"

#include <bit>

#include "util.hpp"

std::pair<std::string, std::string> split_line(const std::string& line) {
    return {line.substr(0, line.size()/2),
            line.substr(line.size()/2, line.size()/2)};
}

uint64_t value_of(char c) {
    if ('a' <= c && c <= 'z') {
        return 1 + c - 'a';
    } else if ('A' <= c && c <= 'Z') {
        return 27 + c - 'A';
    }
}

uint64_t contents(const std::string& contents_string) {
    uint64_t result = 0;

    for (const auto &c: contents_string) {
        result |= (1L << value_of(c));
    }

    return result;
}

int day03_part1(const std::vector<std::string> &input) {
    int accumulator = 0;

    for (const auto& line: input) {
        auto compartments = split_line(line);

        uint64_t left_contents = contents(compartments.first);
        uint64_t right_contents = contents(compartments.second);
        uint64_t common_contents = left_contents & right_contents;

        int value_of_common = std::countr_zero(common_contents);
        accumulator += value_of_common;
    }

    return accumulator;
}

int day03_part2(const std::vector<std::string> &input) {
    return -1;
}


