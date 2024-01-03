#include <vector>
#include <string>
#include <ranges>
#include <cstdint>
#include <iostream>
#include <numeric>

#include "day01.hpp"

constexpr auto is_not_digit = [](char c){ return !isdigit(c); };
constexpr auto find_first_digit = std::views::drop_while(is_not_digit);
constexpr auto find_last_digit = std::views::reverse | find_first_digit;
constexpr auto as_decimal = [](auto&& r) -> int64_t { return *r.begin() - '0'; };
constexpr auto sum_of_first_and_last_digits = [](std::string line) {
    return as_decimal(line | find_first_digit) * 10 +
            as_decimal( line | std::views::reverse | find_first_digit);                        ;
};

int64_t day01_part1(const std::vector<std::string>&input) {
    auto values = input | std::views::transform(sum_of_first_and_last_digits);
    return std::accumulate(values.begin(), values.end(), 0L);
}

int day01_part2(const std::vector<std::string> &input) {
    return 123;
}
