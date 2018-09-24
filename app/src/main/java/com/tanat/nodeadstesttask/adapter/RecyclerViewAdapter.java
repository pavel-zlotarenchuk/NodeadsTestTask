package com.tanat.nodeadstesttask.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanat.nodeadstesttask.R;
import com.tanat.nodeadstesttask.fragment.EditComment;
import com.tanat.nodeadstesttask.model.Declaration;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<Declaration> declarations;
    private List<Declaration> favoriteDeclarationList;
    private RecyclerViewAdapterListener listener;

    private boolean isCheckOnFavorites;

    public RecyclerViewAdapter(Context context, List<Declaration> listNetDeclaration, List<Declaration> listDBDeclaration) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = (RecyclerViewAdapterListener) context;

        if (null != listNetDeclaration) {
            declarations = listNetDeclaration;
            if (null != listDBDeclaration && !listDBDeclaration.isEmpty()) {
                favoriteDeclarationList = listDBDeclaration;
                isCheckOnFavorites = true;
            }
        } else {
            declarations = listDBDeclaration;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_declaration, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Declaration declaration = declarations.get(position);
        holder.nameTextView.setText(declaration.getFirstname() + "\n" + declaration.getLastname());
        holder.positionTextView.setText(declaration.getPlaceOfWork());
        holder.starImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_border32));

        if (isCheckOnFavorites) {
            for (int i = 0; i < favoriteDeclarationList.size(); i++) {
                Declaration favorite = favoriteDeclarationList.get(i);
                if (declaration.getId().equals(favorite.getId())) {
                    holder.starImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star32));
                    declaration.setComment(favorite.getComment());
                    i = favoriteDeclarationList.size();
                }
            }
        } else if (null == favoriteDeclarationList){
            holder.starImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star32));
        }

        if (null != declaration.getComment() && !declaration.getComment().equals("")) {
            holder.commentTextView.setText("Comment:\n" + declaration.getComment());
            holder.commentTextView.setVisibility(View.VISIBLE);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.editComment(declaration, EditComment.EDIT);
                }
            });
        } else {
            holder.commentTextView.setVisibility(View.GONE);
            holder.cardView.setOnClickListener(null);
        }

        holder.bookImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.openPdf(declaration.getLinkPDF());
            }
        });

        holder.starImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.setFavorites(declaration);
            }
        });
    }

    @Override
    public int getItemCount() {
        return declarations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final CardView cardView;
        final TextView nameTextView;
        final TextView positionTextView;
        final TextView commentTextView;
        final ImageView starImageButton;
        final ImageView bookImageButton;

        ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cardView);
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            positionTextView = (TextView) view.findViewById(R.id.positionTextView);
            commentTextView = (TextView) view.findViewById(R.id.commentTextView);
            starImageButton = (ImageView) view.findViewById(R.id.starImageButton);
            bookImageButton = (ImageView) view.findViewById(R.id.bookImageButton);
        }
    }

    public interface RecyclerViewAdapterListener {
        void openPdf(String link);

        void setFavorites(Declaration declaration);

        void editComment(Declaration declaration, int operationType);
    }
}