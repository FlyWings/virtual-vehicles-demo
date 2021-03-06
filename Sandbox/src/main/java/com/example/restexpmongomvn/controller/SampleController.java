package com.example.restexpmongomvn.controller;

import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;

import com.strategicgains.hyperexpress.RelTypes;
import com.strategicgains.hyperexpress.domain.Link;
import com.strategicgains.hyperexpress.domain.LinkableCollection;
import com.strategicgains.hyperexpress.util.LinkUtils;
import com.strategicgains.repoexpress.mongodb.MongodbEntityRepository;
import com.strategicgains.restexpress.Request;
import com.strategicgains.restexpress.Response;
import com.strategicgains.restexpress.common.query.QueryFilter;
import com.strategicgains.restexpress.common.query.QueryOrder;
import com.strategicgains.restexpress.common.query.QueryRange;
import com.strategicgains.restexpress.exception.BadRequestException;
import com.strategicgains.restexpress.query.QueryFilters;
import com.strategicgains.restexpress.query.QueryOrders;
import com.strategicgains.restexpress.query.QueryRanges;
import com.example.restexpmongomvn.Constants;
import com.example.restexpmongomvn.domain.Sample;
import com.strategicgains.syntaxe.ValidationEngine;

/**
 *
 * @author gstafford
 */
public class SampleController {

    private MongodbEntityRepository<Sample> samples;

    /**
     *
     * @param orderRepository
     */
    public SampleController(MongodbEntityRepository<Sample> orderRepository) {
        super();
        this.samples = orderRepository;
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public String create(Request request, Response response) {
        Sample order = request.getBodyAs(Sample.class, "Sample details not provided");
        ValidationEngine.validateAndThrow(order);
        Sample saved = samples.create(order);

        // Construct the response for create...
        response.setResponseCreated();

        // Include the Location header...
        String locationPattern = request.getNamedUrl(HttpMethod.GET, Constants.Routes.SINGLE_VALET);
        response.addLocationHeader(LinkUtils.formatUrl(locationPattern, Constants.Url.VALET_ID, saved.getId()));

        // Return the newly-created ID...
        return saved.getId();
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public Sample read(Request request, Response response) {
        String id = request.getHeader(Constants.Url.VALET_ID, "No Sample ID supplied");
        Sample sample = samples.read(id);

        // Add 'self' link
        String selfPattern = request.getNamedUrl(HttpMethod.GET, Constants.Routes.SINGLE_VALET);
        String selfUrl = LinkUtils.formatUrl(selfPattern, Constants.Url.VALET_ID, sample.getId());
        sample.addLink(new Link(RelTypes.SELF, selfUrl));

        return sample;
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    public LinkableCollection<Sample> readAll(Request request, Response response) {
        QueryFilter filter = QueryFilters.parseFrom(request);
        QueryOrder order = QueryOrders.parseFrom(request);
        QueryRange range = QueryRanges.parseFrom(request, 20);
        List<Sample> results = samples.readAll(filter, range, order);
        long count = samples.count(filter);
        response.setCollectionResponse(range, results.size(), count);

        // Add 'self' links
        String orderSelfPattern = request.getNamedUrl(HttpMethod.GET, Constants.Routes.SINGLE_VALET);

        for (Sample result : results) {
            String selfUrl = LinkUtils.formatUrl(orderSelfPattern, Constants.Url.VALET_ID, result.getId());
            result.addLink(new Link(RelTypes.SELF, selfUrl));
        }

        String selfUrl = request.getNamedUrl(HttpMethod.GET, Constants.Routes.VALET_COLLECTION);
        LinkableCollection<Sample> wrapper = new LinkableCollection<Sample>(results);
        wrapper.addLink(new Link(RelTypes.SELF, selfUrl));
        return wrapper;
    }

    /**
     *
     * @param request
     * @param response
     */
    public void update(Request request, Response response) {
        String id = request.getHeader(Constants.Url.VALET_ID);
        Sample sample = request.getBodyAs(Sample.class, "Sample details not provided");

        if (!id.equals(sample.getId())) {
            throw new BadRequestException("ID in URL and ID in Sample must match");
        }

        ValidationEngine.validateAndThrow(sample);
        samples.update(sample);
        response.setResponseNoContent();
    }

    /**
     *
     * @param request
     * @param response
     */
    public void delete(Request request, Response response) {
        String id = request.getHeader(Constants.Url.VALET_ID, "No Sample ID supplied");
        samples.delete(id);
        response.setResponseNoContent();
    }
}
