package io.openenterprise.daisy.data.spark.sql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nonnull;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.functions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class DatasetPage implements Page<Row> {

    @JsonIgnore
    @Setter
    protected Dataset<Row> dataset;

    @Setter
    protected Pageable pageable;

    @JsonIgnore
    protected Dataset<Row> rowNumberedDataset;

    public DatasetPage(@Nonnull Dataset<Row> dataset, @Nonnull Pageable pageable) {
        if (pageable.isUnpaged()) {
            throw new UnsupportedOperationException();
        }

        this.dataset = dataset;
        this.pageable = pageable;

        var sort = pageable.getSort();

        if (sort.isSorted()) {
            if (sort.stream().anyMatch(Sort.Order::isDescending)) {
                throw new UnsupportedOperationException();
            }

            var sortColumns = sort.stream().map(order -> new Column(order.getProperty())).toArray(Column[]::new);

            this.rowNumberedDataset = dataset
                    .repartitionByRange(getTotalPages(), sortColumns)
                    .withColumn("partitionId", functions.spark_partition_id())
                    .withColumn("partitionRowNumber", functions.row_number().over(Window.partitionBy(
                            "partitionId").orderBy(sortColumns)))
                    .withColumn("rowNumber", new Column("partitionId").multiply(pageable.getPageSize())
                            .plus(new Column("partitionRowNumber")));
        } else {
            this.rowNumberedDataset = dataset
                    .repartition(getTotalPages())
                    .withColumn("partitionId", functions.spark_partition_id())
                    .withColumn("partitionRowNumber", functions.row_number().over(Window.partitionBy(
                            "partitionId").orderBy("partitionId")))
                    .withColumn("rowNumber", new Column("partitionId").multiply(pageable.getPageSize()).plus(
                            new Column("partitionRowNumber")));
        }
    }

    @Override
    public int getTotalPages() {
        return (int) Math.floorDiv(getTotalElements(), getSize()) +
                (Math.floorMod(getTotalElements(), getSize()) > 0 ? 1 : 0);
    }

    @Override
    public long getTotalElements() {
        return dataset.count();
    }

    @Override
    public int getNumber() {
        return this.pageable.isPaged() ? this.pageable.getPageNumber() : 0;
    }

    @Override
    public int getSize() {
        return this.pageable.isPaged() ? this.pageable.getPageSize() : (int) getTotalElements();
    }

    @Override
    public int getNumberOfElements() {
        return getSize();
    }

    @Nonnull
    @Override
    public List<Row> getContent() {
        var startRowNumber = pageable.getPageNumber() == 0? 0 : pageable.getPageNumber() * pageable.getPageSize();
        var endRowNumber = ((pageable.getPageNumber() + 1) * pageable.getPageSize());

        return rowNumberedDataset.where(new Column("rowNumber").between(startRowNumber, endRowNumber))
                .collectAsList();
    }

    @Override
    public boolean hasContent() {
        return !dataset.isEmpty();
    }

    @Nonnull
    @Override
    public Sort getSort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFirst() {
        return !this.hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !this.hasNext();
    }

    @Override
    public boolean hasNext() {
        return this.getPageable().getPageNumber() < this.getTotalPages() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return this.getPageable().hasPrevious();
    }

    @Nonnull
    @Override
    public Pageable getPageable() {
        return PageRequest.of(this.getNumber(), this.getSize(), Sort.unsorted());
    }

    @Nonnull
    @Override
    public Pageable nextPageable() {
        return this.hasNext() ? this.pageable.next() : this.pageable;
    }

    @Nonnull
    @Override
    public Pageable previousPageable() {
        return this.pageable.previousOrFirst();
    }

    @Override
    public <U> Page<U> map(Function<? super Row, ? extends U> converter) {
        throw new NotImplementedException();
    }

    @Override
    public @Nonnull Iterator<Row> iterator() {
        throw new NotImplementedException();
    }
}
