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


constexpr std::array<std::string_view,19> digits = {
    "0","1","2","3","4","5","6","7","8","9",
    "one","two","three","four","five","six","seven","eight","nine"
};

int64_t to_digit(std::string_view str) {
    // Find the digit which is prefix of our current string (if any)
    auto match = std::ranges::find_if(digits, [&](auto&& digit){
        return str.starts_with(digit);
    });
    if (match == digits.end()) return -1;

    // Turn the position in the array of digits into a digit
    int64_t digit = std::distance(digits.begin(), match);
    if (digit >= 10) return digit - 9;
    return digit;
}

int64_t day01_part1(const std::vector<std::string>& input) {
    auto values = input | std::views::transform(sum_of_first_and_last_digits);

    return std::accumulate(values.begin(), values.end(), 0L);
}

int64_t first_and_last_by_part2_rules(std::string_view line) {
    int64_t first = -1;
    int64_t last = -1;

    for (auto pos : std::views::iota(line.begin(), line.end())) {

        auto substr = std::string_view(pos, line.end());

        if (int64_t digit = to_digit(substr); digit != -1) {
            if (first == -1)
                first = digit;
            last = digit;
        }
    }
    return first*10 + last;
}

int64_t day01_part2(const std::vector<std::string> &input) {
    auto values = input | std::views::transform(first_and_last_by_part2_rules);

    return std::accumulate(values.begin(), values.end(), 0L);
}
