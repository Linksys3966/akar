(ns akar.patterns.collection-test
  (:require [akar.patterns.collection :refer :all]
            [akar.patterns.basic :refer :all]
            [akar.primitives :refer :all]
            [akar.combinators :refer :all]
            [clojure.test :refer :all]))

(deftest collection-patterns-test
  (let [block (clauses
                !empty (fn [] :empty)
                !cons (fn [hd tl] {:hd hd :tl tl})
                !any (fn [] :not-sequential))]

    (testing "!empty"
      (is (= :empty
             (match [] block))))

    (testing "!cons"
      (is (= {:hd 3 :tl [4 5]}
             (match [3 4 5] block))))

    (testing "non-sequential data fallthrough for both !empty and !cons"
      (is (= :not-sequential
             (match :some-random-data block)))))

  (let [block (clauses
                (!and (!key :k) (!optional-key :l) (!optional-key :m)) (fn [a b c] [a b c])
                !any (fn [] :stuff))]

    (testing "!key"

      (is (= [:x :y nil]
             (match {:k :x :l :y} block)))

      (is (= :stuff
             (match [] block))))))