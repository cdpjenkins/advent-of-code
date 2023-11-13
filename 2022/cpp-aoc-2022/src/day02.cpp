#include "day02.hpp"

#include <numeric>
#include <ranges>
#include <stdexcept>

#include "util.hpp"

using namespace std::string_literals;

constexpr int SCORE_LOSE = 0;
constexpr int SCORE_DRAW = 3;
constexpr int SCORE_WIN = 6;

constexpr int SCORE_ROCK = 1;
constexpr int SCORE_PAPER = 2;
constexpr int SCORE_SCISSORS = 3;

static int score_following_choice(const std::string& input) {
    if (input == "A X"s) return SCORE_ROCK     + SCORE_DRAW;
    if (input == "A Y"s) return SCORE_PAPER    + SCORE_WIN;
    if (input == "A Z"s) return SCORE_SCISSORS + SCORE_LOSE;
    if (input == "B X"s) return SCORE_ROCK     + SCORE_LOSE;
    if (input == "B Y"s) return SCORE_PAPER    + SCORE_DRAW;
    if (input == "B Z"s) return SCORE_SCISSORS + SCORE_WIN;
    if (input == "C X"s) return SCORE_ROCK     + SCORE_WIN;
    if (input == "C Y"s) return SCORE_PAPER    + SCORE_LOSE;
    if (input == "C Z"s) return SCORE_SCISSORS + SCORE_DRAW;
    throw std::runtime_error(input);
}

static int score_following_result(const std::string& input) {
    if (input == "A X"s) return SCORE_SCISSORS + SCORE_LOSE;
    if (input == "A Y"s) return SCORE_ROCK     + SCORE_DRAW;
    if (input == "A Z"s) return SCORE_PAPER    + SCORE_WIN;
    if (input == "B X"s) return SCORE_ROCK     + SCORE_LOSE;
    if (input == "B Y"s) return SCORE_PAPER    + SCORE_DRAW;
    if (input == "B Z"s) return SCORE_SCISSORS + SCORE_WIN;
    if (input == "C X"s) return SCORE_PAPER    + SCORE_LOSE;
    if (input == "C Y"s) return SCORE_SCISSORS + SCORE_DRAW;
    if (input == "C Z"s) return SCORE_ROCK     + SCORE_WIN;
    throw std::runtime_error(input);
}

int day02_part1_rock_paper_scissors_following_choice(const std::vector<std::string> &input) {
    auto scores = input | std::views::transform(score_following_choice);
    return std::reduce(scores.begin(), scores.end());
}

int day02_part2_rock_paper_scissors_following_result(const std::vector<std::string> &input) {
    auto scores = input | std::views::transform(score_following_result);
    return std::reduce(scores.begin(), scores.end());
}
