(ns akar.combinators-test
  (:require [clojure.test :refer :all]
            [akar.patterns.basic :refer :all]
            [akar.patterns.collection :refer :all]
            [akar.combinators :refer :all]
            [akar.primitives :refer :all]))

(deftest combinators-test

  (testing "!and"

    (testing "fails if any of the patterns fails"
      (is (= nil
             ((!and !any !var !fail) 9))))

    (testing "gives all the extracts when all patterns succeed"
      (is (= [9 9]
             ((!and !any !var !var) 9)))))

  (testing "!at"

    (testing "if matched, gives the same input"
      (is (= 2
             (try-match 2 (clauses
                            (!at (!pred even?)) (fn [x] x))))))

    (testing "if not matched, gives nothing"
      (is (= clause-not-applied
             (try-match 3 (clauses
                            (!at (!pred even?)) (fn [x] x)))))))

  (testing "!guard"

    (testing "original pattern succeeds, only if guard succeeds too"
      (is (= :even-and-2
             (try-match 2 (clauses
                            (!guard (!pred even?) (partial = 2)) (fn [] :even-and-2))))))

    (testing "original pattern fails, if the guard fails"
      (is (= clause-not-applied
             (try-match 4 (clauses
                            (!guard (!pred even?) (partial = 2)) (fn [] :even-and-2)))))))

  (testing "!or"

    (testing "fails if no pattern succeeds"
      (is (= nil
             ((!or !fail !fail !fail) 9))))

    (testing "gives extracts from the first matched pattern"
      (is (= []
             ((!or !any !var) 9)))
      (is (= [9]
             ((!or !var !any) 9)))))

  (testing "!not"

    (testing "reverse a good match"
      (is (= nil
             ((!not !var) 9))))

    (testing "reverses a bad match"
      (is (= []
             ((!not !fail) 9))))))
